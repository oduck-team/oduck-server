package io.oduck.api.domain.weekly.repository;

import static io.oduck.api.domain.anime.entity.QAnime.anime;
import static io.oduck.api.domain.weekly.entity.QWeeklyAnime.weeklyAnime;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.weekly.dto.WeeklyDsl.WeeklyAnimeDsl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WeeklyAnimeRepositoryCustomImpl implements WeeklyAnimeRepositoryCustom{
    private final JPAQueryFactory query;

    @Override
    public List<WeeklyAnimeDsl> getWeeklyAnime() {
        JPAQuery<WeeklyAnimeDsl> jpaQuery = query
            .select(
                Projections.constructor(
                    WeeklyAnimeDsl.class,
                    anime.id,
                    anime.title,
                    anime.thumbnail,
                    anime.starRatingScoreTotal,
                    anime.starRatingCount,
                    anime.starRatingScoreTotal.doubleValue().divide(anime.starRatingCount).multiply(0.2)
                        .add(weeklyAnime.reviewCount.doubleValue().multiply(0.3))
                        .add(weeklyAnime.bookmarkCount.doubleValue().multiply(0.3))
                        .add(weeklyAnime.viewCount.doubleValue().multiply(0.2))
                        .as("rankScore")
                )
            )
            .from(weeklyAnime)
            .leftJoin(anime)
            .on(weeklyAnime.anime.id.eq(anime.id).and(anime.isReleased.isTrue()).and(anime.deletedAt.isNull()))
            .groupBy(anime.id)
            .limit(10)
            .orderBy(Expressions.numberPath(Double.class, "rankScore").desc());

        return jpaQuery.fetch();
    }
}
