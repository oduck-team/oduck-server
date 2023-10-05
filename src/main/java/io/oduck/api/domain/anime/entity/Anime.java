package io.oduck.api.domain.anime.entity;

import io.oduck.api.domain.series.entity.Series;
import io.oduck.api.global.audit.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Anime extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String title;

  @Column(nullable = false, length = 255)
  private String summary;

  @Enumerated(EnumType.STRING)
  private BroadcastType broadcastType;

  @Column(nullable = false)
  private int episodeCount;

  @Column(nullable = true, length = 500)
  private String thumbnail;

  private int year;

  @Enumerated(EnumType.STRING)
  private Quarter quarter;

  @Enumerated(EnumType.STRING)
  private Rating rating;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false)
  private boolean isReleased;

  @ColumnDefault("0")
  private long viewCount;

  @ColumnDefault("0")
  private long reviewCount;

  @ColumnDefault("0")
  private long bookmarkCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "series_id")
  private Series series;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<AnimeOriginalAuthor> animeOriginalAuthors;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<AnimeStudio> animeStudios;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<AnimeVoiceActor> animeVoiceActors;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<AnimeGenre> animeGenres;
}
