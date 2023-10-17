package io.oduck.api.domain.anime.entity;

import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import io.oduck.api.global.audit.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimeOriginalAuthor extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "anime_id")
  private Anime anime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "original_author_id")
  private OriginalAuthor originalAuthor;

  public void setAnime(Anime anime) {
    this.anime = anime;
  }

  /**
   * M:N 관계 연결 테이블(연결 엔티티) 생성 시 빌더를 사용하면 애니의 연관 관계도 생성할 때 추가할 수 있어
   * 생성 메소드로 구현
   */
  public static AnimeOriginalAuthor createAnimeOriginalAuthor(OriginalAuthor originalAuthor){
    AnimeOriginalAuthor animeOriginalAuthor = new AnimeOriginalAuthor();
    animeOriginalAuthor.originalAuthor = originalAuthor;
    return animeOriginalAuthor;
  }
}