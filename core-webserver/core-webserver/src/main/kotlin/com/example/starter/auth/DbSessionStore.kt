package com.example.starter.auth

import com.example.starter.auth.db.entity.DbSessionEntity
import com.example.starter.auth.db.entity.DbSessionEntity_
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.VertxContextPRNG
import io.vertx.ext.web.Session
import io.vertx.ext.web.sstore.SessionStore
import io.vertx.ext.web.sstore.impl.SharedDataSessionImpl
import jakarta.persistence.EntityNotFoundException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import mu.KotlinLogging
import org.hibernate.SessionFactory

class DbSessionStore private constructor(private val sessionFactory: SessionFactory) : SessionStore {

    companion object {
        const val DEFAULT_LENGTH = 16

        fun create(vertx: Vertx, sessionFactory: SessionFactory) =
            DbSessionStore(sessionFactory).init(vertx, JsonObject())
    }

    lateinit var prng: VertxContextPRNG

    private val logger = KotlinLogging.logger {}

    override fun init(vertx: Vertx, options: JsonObject?): SessionStore {
        prng = VertxContextPRNG.current(vertx)
        return this
    }

    override fun retryTimeout(): Long {
        return 0
    }

    override fun createSession(timeout: Long): Session {
        return createSession(timeout, DEFAULT_LENGTH)
    }

    override fun createSession(timeout: Long, length: Int): Session {
        return SharedDataSessionImpl(prng, timeout, length)
    }

    override fun get(id: String): Future<Session?> = fromTransaction {
        val entity = it.get(DbSessionEntity::class.java, id) ?: null
        entity?.contents?.let {
            SharedDataSessionImpl(prng).apply {
                readFromBuffer(0, Buffer.buffer(it))
            }
        }
    }

    override fun delete(id: String): Future<Void> = fromTransaction {
        val ref = it.getReference(DbSessionEntity::class.java, id)
        try {
            it.remove(ref)
            it.flush()
        } catch (e: EntityNotFoundException) {
            logger.trace { "Session $id not found to delete, ignoring" }
        }
    }.mapEmpty()


    override fun put(session: Session): Future<Void> = fromTransaction {
        val entity = it.get(DbSessionEntity::class.java, session.id()) ?: null
        val old = entity?.contents?.let {
            SharedDataSessionImpl(prng).apply {
                readFromBuffer(0, Buffer.buffer(it))
            }
        }
        val new = session as SharedDataSessionImpl
        if (old != null) {
            // there was already some stored data in this case we need to validate versions
            check(old.version() == new.version())
        }
        new.incrementVersion()
        val buffer = Buffer.buffer()
        new.writeToBuffer(buffer)
        if (entity != null) {
            entity.apply { contents = buffer.bytes }
        } else {
            val newEntity = DbSessionEntity().apply {
                id = session.id()
                contents = buffer.bytes
            }
            it.merge(newEntity)
        }
        it.flush()
    }.mapEmpty()


    override fun clear(): Future<Void> = fromTransaction {
        val builder = sessionFactory.criteriaBuilder
        val q = builder.createCriteriaDelete(DbSessionEntity::class.java)
        q.from(DbSessionEntity::class.java)
        it.createMutationQuery(q).executeUpdate()
    }.mapEmpty()

    override fun size(): Future<Int> = fromTransaction {
        val builder = sessionFactory.criteriaBuilder
        val q = builder.createQuery(Long::class.java)
        val root = q.from(DbSessionEntity::class.java)
        q.select(builder.count(root.get(DbSessionEntity_.id)))
        (it.createQuery(q).singleResult).toInt()
    }

    private fun <T> fromTransaction(block: (org.hibernate.Session) -> T): Future<T> =
        Future.fromCompletionStage(
            CoroutineScope(Dispatchers.IO).async {
                sessionFactory.fromTransaction(block)
            }.asCompletableFuture()
        )

    override fun close() {
        sessionFactory.close()
    }
}