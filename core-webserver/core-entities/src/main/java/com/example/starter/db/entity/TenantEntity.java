package com.example.starter.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

@Entity
public class TenantEntity {

  @Id
  @GeneratedValue
  @Nullable
  public UUID id;
  @Column(nullable = false)
  public String name;

  public TenantEntity(String name) {
    this.name = name;
  }

  TenantEntity() {
  }
}
