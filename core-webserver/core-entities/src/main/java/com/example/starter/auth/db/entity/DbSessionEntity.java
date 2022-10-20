package com.example.starter.auth.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "Session")
public class DbSessionEntity {

    @Id
    @Nullable
    public String id;

    @Column(nullable = false, length = 65535)
    public byte[] contents;
}
