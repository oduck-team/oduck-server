package io.oduck.api.golbal.audit;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;

public abstract class UpdatableEntity extends BaseEntity {
  @UpdateTimestamp
  @Column(nullable = false)
  protected LocalDateTime updatedAt;
}
