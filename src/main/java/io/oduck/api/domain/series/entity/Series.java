package io.oduck.api.domain.series.entity;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.global.audit.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Series extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String title;

  @OneToMany(mappedBy = "series", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @Builder.Default
  private List<Anime> animes = new ArrayList<>();

  public void update(String title) {
    this.title = title;
  }

  public void delete() {
    this.animes.clear();
    this.deletedAt = LocalDateTime.now();
  }
}
