package io.oduck.api.golbal.audit;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

public abstract class DeletableEntity extends UpdatableEntity {
  @Column(nullable = true)
  protected LocalDateTime deletedAt;
}
