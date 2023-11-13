package io.oduck.api.domain.genre.entity;

import io.oduck.api.domain.anime.entity.AnimeGenre;
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
public class Genre extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 15)
  private String name;

  @OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @Builder.Default
  private List<AnimeGenre> animeGenres = new ArrayList<>();

  public void update(String name) {
    this.name = name;
  }

  public void delete() {
    this.animeGenres.clear();
    this.deletedAt = LocalDateTime.now();
  }
}