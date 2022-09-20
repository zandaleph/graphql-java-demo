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
@Table(
    name = "Domain",
    indexes = {
        @Index(name = "idx_domain_tenant_domain_name", columnList = "tenant_id,domain_name", unique = true)}
)
public class DomainEntity {

  @Id
  @GeneratedValue
  @Nullable
  public UUID id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tenant_id", nullable = false, updatable = false)
  public TenantEntity tenant;
  @Column(nullable = false, name = "domain_name")
  public String domainName;
  @Column(nullable = false)
  public boolean enabled;

  public DomainEntity(TenantEntity tenant, String domainName, boolean enabled) {
    this.tenant = tenant;
    this.domainName = domainName;
    this.enabled = enabled;
  }

  DomainEntity() {
  }
}
