package com.example.starter.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(indexes = {
    @Index(name = "idx_user_tenant_username", columnList = "tenant_id,name", unique = true)})
public class UserEntity {

  @Id
  @GeneratedValue
  @Nullable
  public UUID id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tenant_id", nullable = false, updatable = false)
  public TenantEntity tenant;
  @Column(nullable = false)
  public String name;

  public UserEntity(TenantEntity tenant, String name) {
    this.tenant = tenant;
    this.name = name;
  }

  UserEntity() {
  }
}
