package io.oduck.api.global.audit;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class UpdatableEntity extends BaseEntity {
  @UpdateTimestamp
  @Column(nullable = false)
  protected LocalDateTime updatedAt;
}
