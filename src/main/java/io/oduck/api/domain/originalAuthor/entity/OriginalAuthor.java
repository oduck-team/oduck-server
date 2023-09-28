package io.oduck.api.domain.originalAuthor.entity;

import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OriginalAuthor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @OneToMany(mappedBy = "originalAuthor", cascade = CascadeType.PERSIST)
  private List<AnimeOriginalAuthor> animeOriginalAuthors;
}
