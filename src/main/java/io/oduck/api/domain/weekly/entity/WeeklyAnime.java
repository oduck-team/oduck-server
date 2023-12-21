package io.oduck.api.domain.weekly.entity;

import io.oduck.api.domain.anime.entity.Anime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeeklyAnime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id")
    private Anime anime;

    @ColumnDefault("0")
    @Builder.Default
    private Long reviewCount = 0L;

    @ColumnDefault("0")
    @Builder.Default
    private Long bookmarkCount = 0L;

    @ColumnDefault("0")
    @Builder.Default
    private Long viewCount = 0L;

    @Builder
    public WeeklyAnime(Anime anime) {
        this.anime = anime;
        anime.updateWeeklyAnime(this);
    }

    public static WeeklyAnime createWeeklyAnime(Anime anime) {
        return WeeklyAnime.builder()
                .anime(anime)
                .build();
    }

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
}
