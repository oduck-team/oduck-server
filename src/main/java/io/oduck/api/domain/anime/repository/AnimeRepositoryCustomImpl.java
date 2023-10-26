package io.oduck.api.domain.anime.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.anime.dto.AnimeReq;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.global.common.OrderDirection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static io.oduck.api.domain.anime.dto.AnimeReq.EpisodeCountEnum;
import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;
import static io.oduck.api.domain.anime.entity.QAnime.anime;
import static io.oduck.api.domain.anime.entity.QAnimeGenre.animeGenre;
import static io.oduck.api.global.utils.QueryDslUtils.fetchSliceByCursor;
import static org.springframework.data.domain.Sort.Direction;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AnimeRepositoryCustomImpl implements AnimeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<SearchResult> findAnimesByCondition(String query, String cursor, AnimeReq.Sort sort, OrderDirection order, Pageable pageable, SearchFilterDsl searchFilterDsl) {
        JPAQuery<SearchResult> jpaQuery = queryFactory
                .select(
                        Projections.constructor(
                                SearchResult.class,
                                anime.id,
                                anime.title,
                                anime.thumbnail,
                                anime.starRatingScoreTotal,
                                anime.starRatingCount
                        )
                )
                .from(anime)
                .leftJoin(anime.animeGenres, animeGenre)
                .where(
                        titleEq(query),
                        genreIdsIn(searchFilterDsl.getGenreIds()),
                        broadcastTypesIn(searchFilterDsl.getBroadcastTypes()),
                        compareEpisodeCount(searchFilterDsl.getEpisodeCountEnums()),
                        yearsIn(searchFilterDsl.getYears()),
                        currentYearsAndQuarters(searchFilterDsl.getQuarters()),
                        cursorCondition(cursor, pageable),
                        notDeleted()
                )
                .limit(pageable.getPageSize());

        return fetchSliceByCursor(anime, jpaQuery, pageable);
    }

    private BooleanExpression genreIdsIn(List<Long> genreIds) {

        return isListEmpty(genreIds) ? null : animeGenre.genre.id.in(genreIds);
    }

    private BooleanExpression cursorCondition(String cursor, Pageable pageable) {
        if (StringUtils.isNullOrEmpty(cursor)) {
            return null;
        }

        boolean isNumeric = cursor.chars().allMatch(Character::isDigit);

        if(!isNumeric){
            return null;
        }

        List<Sort.Order> orders = pageable.getSort().get().collect(Collectors.toList());
        Direction direction = orders.get(0).getDirection();

        if(direction == Direction.ASC){
            return anime.id.gt(Long.parseLong(cursor));
        }else{
            return anime.id.lt(Long.parseLong(cursor));
        }
    }

    private static BooleanExpression notDeleted() {
        return anime.deletedAt.isNull();
    }

    private BooleanExpression currentYearsAndQuarters(List<Quarter> quarters) {
        if(isListEmpty(quarters)){
            return null;
        }

        int currentYear = LocalDate.now().getYear();

        BooleanExpression expression = null;
        for (Quarter quarter : quarters) {
            BooleanExpression and = anime.year.eq(currentYear).and(anime.quarter.eq(quarter));
            if(expression == null){
                expression = and;
            }else{
                expression.and(and);
            }
        }

        return expression;
    }

    private BooleanExpression yearsIn(List<Integer> years) {
        return isListEmpty(years) ? null : anime.year.in(years);
    }

    private BooleanExpression compareEpisodeCount(List<EpisodeCountEnum> episodeCountEnums) {
        if (isListEmpty(episodeCountEnums)) {
            return null;
        }

        // EpisodeCountEnum.OVER_HUNDRED가 없으면 goe 부분은 null이 되어야 함
        Integer overHundredCount = episodeCountEnums.stream()
                .filter(e -> e == EpisodeCountEnum.OVER_HUNDRED)
                .findFirst()
                .map(EpisodeCountEnum::getCount)
                .orElse(null);

        // OVER_HUNDRED를 제외한 enum 중 가장 큰 count 값 찾기
        Integer maxCount = episodeCountEnums.stream()
                .filter(e -> e != EpisodeCountEnum.OVER_HUNDRED)
                .map(EpisodeCountEnum::getCount)
                .max(Integer::compareTo)
                .orElse(null);

        // BooleanExpression 조건 설정
        BooleanTemplate loeTemplate = (BooleanTemplate) anime.episodeCount.loe(maxCount);
        BooleanTemplate goeTemplate = (overHundredCount != null) ? (BooleanTemplate) anime.episodeCount.goe(overHundredCount) : null;

        // loe와 goe 조건을 조합하여 반환
        if (goeTemplate != null) {
            return loeTemplate.and(goeTemplate);
        } else {
            return loeTemplate;
        }
    }

    private BooleanExpression broadcastTypesIn(List<BroadcastType> broadcastTypes) {
        return isListEmpty(broadcastTypes) ? null : anime.broadcastType.in(broadcastTypes);
    }

    private BooleanExpression titleEq(String query) {
        return StringUtils.isNullOrEmpty(query) ?  null : anime.title.contains(query);
    }

    private <T> boolean isListEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}
