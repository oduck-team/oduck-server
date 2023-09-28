package io.oduck.api.domain.anime.entity;

import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.bookmark.entity.Bookmark;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.starRating.entity.StarRating;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;

import io.oduck.api.domain.anime.dto.AnimeReq;
import io.oduck.api.domain.series.entity.Series;
import io.oduck.api.global.audit.DeletableEntity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Anime extends DeletableEntity {

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

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<Bookmark> bookMarks;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<ShortReview> shortReviews;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<StarRating> starRatings;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST)
  private List<AttractionPoint> attractionPoints;

  static public Anime creatAnime(AnimeReq req, Series series) {
    Anime anime = new Anime();
    anime.broadcastType = req.getBroadcastType();
    anime.episodeCount = req.getEposideCount();
    anime.quarter = req.getQuarter();
    anime.rating = req.getRating();
    anime.status = req.getStatus();
    anime.summary = req.getSummary();
    anime.thumbnail = req.getThumbnail();
    anime.title = req.getTitle();
    anime.year = req.getYear();
    anime.isReleased = false;
    anime.viewCount = 0;
    anime.reviewCount = 0;
    anime.series = series;
    return anime;
  }
}
