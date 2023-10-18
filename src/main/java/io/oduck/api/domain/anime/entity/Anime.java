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
import java.util.ArrayList;
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

  @Column(nullable = false, length = 600)
  private String summary;

  @Enumerated(EnumType.STRING)
  private BroadcastType broadcastType;

  @Column(nullable = false)
  private int episodeCount;

  @Column(nullable = true, length = 500)
  private String thumbnail;

  @Column(name = "release_year")
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

  @ColumnDefault("0")
  private long starRatingScoreTotal;

  @ColumnDefault("0")
  private long starRatingCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "series_id")
  private Series series;

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<AnimeStudio> animeStudios = new ArrayList<>();

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();

  @OneToMany(mappedBy = "anime", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<AnimeGenre> animeGenres = new ArrayList<>();

  /**
   * 비즈니스 메소드
   */
  // 조회수 증가
  public void increaseViewCount(){
    viewCount++;
  }

  // 리뷰수 증가(짧은 리뷰, 장문 리뷰)
  public void increaseReviewCount(){
    reviewCount++;
  }

  // 리뷰수 감소(짧은 리뷰, 장문 리뷰)
  public void decreaseReviewCount(){
    reviewCount--;
  }

  // 북마크수 증가
  public void increaseBookmarkCount(){
    bookmarkCount++;
  }

  // 북마크수 감소
  public void decreaseBookmarkCount(){
    bookmarkCount--;
  }

  // 애니 공개 전환
  public void releaseAnime(){
    isReleased = true;
  }

  // 비공개 전환
  public void setPrivateAnime(){
    isReleased = false;
  }

  // 평가할 때 점수 합산(별점)
  public void increaseStarRatingScore(int score){
    if(score <= 0){
      throw new IllegalArgumentException("음수는 올 수 없습니다.");
    }
    starRatingScoreTotal += score;
    increaseStarRatingCount();
  }

  // 평가를 지웠을 때 점수 합산(별점)
  public void decreaseStarRatingScore(int score){
    if(score <= 0){
      throw new IllegalArgumentException("음수는 올 수 없습니다.");
    }
    starRatingScoreTotal -= score;
    decreaseStarRatingCount();
  }

  private void increaseStarRatingCount() {
    starRatingCount++;
  }

  private void decreaseStarRatingCount() {
    starRatingCount--;
  }

  /**
   * 연관 관계 관련한 메소드
   */
  // 애니와 원작 작가의 연결 엔티티의 애니 연관 관계 추가 로직
  private void addAnimeOriginalAuthor(AnimeOriginalAuthor animeOriginalAuthor){
    this.animeOriginalAuthors.add(animeOriginalAuthor);
    //TODO: set 대신 다른 이름
    animeOriginalAuthor.setAnime(this);
  }

  // 애니와 스튜디오의 연결 엔티티의 애니 연관 관계 추가 로직
  private void addAnimeStudio(AnimeStudio animeStudio){
    this.animeStudios.add(animeStudio);
    //TODO: set 대신 다른 이름
    animeStudio.setAnime(this);
  }

  // 애니와 성우의 연결 엔티티의 애니 연관 관계 추가 로직
  private void addAnimeVoiceActor(AnimeVoiceActor animeVoiceActor){
    this.animeVoiceActors.add(animeVoiceActor);
    //TODO: set 대신 다른 이름
    animeVoiceActor.setAnime(this);
  }

  // 애니와 장르의 연결 엔티티의 애니 연관 관계 추가 로직
  private void addAnimeGenre(AnimeGenre animeGenre){
    this.animeGenres.add(animeGenre);
    //TODO: set 대신 다른 이름
    animeGenre.setAnime(this);
  }

  private boolean isEntityListElementsExist(List<? extends BaseEntity> elements) {
    return !elements.isEmpty();
  }

  public void updateAnimeOriginalAuthors(List<AnimeOriginalAuthor> animeOriginalAuthors){
    // 만약 현재 animeOriginalAuthors에 값이 존재한다면 List를 비운다.
    // (orphanRemoval = true 설정으로 AnimeOriginalAuthor 테이블의 값들이 delete됨)
    if(isEntityListElementsExist(this.animeOriginalAuthors)){
      this.animeOriginalAuthors.clear();
    }

    for (AnimeOriginalAuthor animeOriginalAuthor : animeOriginalAuthors) {
      addAnimeOriginalAuthor(animeOriginalAuthor);
    }
  }

  public void updateAnimeStudios(List<AnimeStudio> animeStudios) {
    // 만약 현재 animeStudios에 값이 존재한다면 List를 비운다.
    // (orphanRemoval = true 설정으로 AnimeStudio 테이블의 값들이 delete됨)
    if(isEntityListElementsExist(this.animeStudios)){
      this.animeStudios.clear();
    }

    for (AnimeStudio animeStudio : animeStudios) {
      addAnimeStudio(animeStudio);
    }
  }

  public void updateAnimeVoiceActors(List<AnimeVoiceActor> animeVoiceActors) {
    // 만약 현재 animeVoiceActors의 값이 존재한다면 List를 비운다.
    // (orphanRemoval = true 설정으로 animeVoiceActor 테이블의 값들이 delete됨)
    if(isEntityListElementsExist(this.animeVoiceActors)){
      this.animeVoiceActors.clear();
    }

    for (AnimeVoiceActor animeVoiceActor : animeVoiceActors) {
      addAnimeVoiceActor(animeVoiceActor);
    }
  }

  public void updateAnimeGenre(List<AnimeGenre> animeGenres) {
    // 만약 현재 animeGenre의 값이 존재한다면 List를 비운다.
    // (orphanRemoval = true 설정으로 animeGenre의 테이블의 값들이 delete됨)
    if(isEntityListElementsExist(this.animeGenres)){
      this.animeGenres.clear();
    }

    for (AnimeGenre animeGenre : animeGenres) {
      addAnimeGenre(animeGenre);
    }
  }

  public static Anime createAnime(String title, String summary, BroadcastType broadcastType, int episodeCount,
      String thumbnail, int year, Quarter quarter, Rating rating, Status status,
      List<AnimeOriginalAuthor> animeOriginalAuthors, List<AnimeStudio> animeStudios, List<AnimeVoiceActor> animeVoiceActors, List<AnimeGenre> animeGenres, Series series) {
    Anime anime = new Anime();
    // dto의 값으로 anime 초기화
    anime.title = title;
    anime.summary = summary;
    anime.broadcastType = broadcastType;
    anime.episodeCount = episodeCount;
    anime.thumbnail = thumbnail;
    anime.year = year;
    anime.quarter = quarter;
    anime.rating = rating;
    anime.status = status;
    anime.isReleased = false;
    anime.viewCount = 0;
    anime.reviewCount = 0;
    anime.bookmarkCount = 0;
    anime.starRatingScoreTotal = 0;
    anime.starRatingCount = 0;
    
    // 연결 엔티티 설정
    anime.updateAnimeOriginalAuthors(animeOriginalAuthors);
    anime.updateAnimeStudios(animeStudios);
    anime.updateAnimeVoiceActors(animeVoiceActors);
    anime.updateAnimeGenre(animeGenres);
    
    // 시리즈 설정
    anime.series = series;
    return anime;
  }
}