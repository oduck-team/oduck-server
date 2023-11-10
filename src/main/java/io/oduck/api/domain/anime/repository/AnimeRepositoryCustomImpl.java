package io.oduck.api.domain.anime.repository;

import static io.oduck.api.domain.anime.dto.AnimeReq.EpisodeCountEnum;
import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;
import static io.oduck.api.domain.anime.entity.QAnime.anime;
import static io.oduck.api.domain.anime.entity.QAnimeGenre.animeGenre;
import static io.oduck.api.global.utils.QueryDslUtils.fetchSliceByCursor;
import static org.springframework.data.domain.Sort.Direction;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.QAnime;
import io.oduck.api.domain.anime.entity.Quarter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AnimeRepositoryCustomImpl implements AnimeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<SearchResult> findSliceByCondition(String query, String cursor, Pageable pageable, SearchFilterDsl searchFilterDsl) {
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
                yearFilters(searchFilterDsl.getQuarters(), searchFilterDsl.getYears()),
                statusIn(searchFilterDsl.getStatuses()),
                cursorCondition(cursor, pageable),
                isReleased(true),
                notDeleted()
            )
            .groupBy(anime.id)
            .limit(pageable.getPageSize());

        return fetchSliceByCursor(sortPath(anime), jpaQuery, pageable);
    }

    private BooleanExpression yearFilters(List<Quarter> quarters, List<Integer> years) {
        BooleanExpression firstExpression = currentYearsAndQuarters(quarters);
        BooleanExpression secondExpression  = yearsIn(years);

        if (firstExpression == null && secondExpression == null) {
            return null;
        }

        if (firstExpression == null) {
            return secondExpression;
        }

        if (secondExpression == null) {
            return firstExpression;
        }

        return firstExpression.or(secondExpression);
    }

    private BooleanExpression isReleased(boolean isReleased) {
        return anime.isReleased.eq(isReleased);
    }

    private List<Path> sortPath(QAnime anime) {
        List<Path> path = new ArrayList<>();
        path.add(anime);
        return path;
    }

    private BooleanExpression statusIn(List<Status> statuses) {
        return isListEmpty(statuses) ? null : anime.status.in(statuses);
    }

    private BooleanExpression genreIdsIn(List<Long> genreIds) {

        return isListEmpty(genreIds) ? null : animeGenre.genre.id.in(genreIds);
    }

    private BooleanExpression cursorCondition(String cursor, Pageable pageable) {
        if (StringUtils.isNullOrEmpty(cursor)) {
            return null;
        }

        List<Sort.Order> orders = pageable.getSort().get().collect(Collectors.toList());
        Direction direction = orders.get(0).getDirection();

        if (direction == Direction.ASC) {
            return anime.title.gt(cursor);
        }else{
            return anime.title.lt(cursor);
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
            BooleanExpression or = anime.year.eq(currentYear).and(anime.quarter.eq(quarter));
            if(expression == null){
                expression = or;
            }else{
                expression = expression.or(or);
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
        BooleanExpression loeExpression = anime.episodeCount.loe(maxCount);
        BooleanExpression goeExpression = (overHundredCount != null) ? anime.episodeCount.goe(overHundredCount) : null;

        // loe와 goe 조건을 조합하여 반환
        if (goeExpression != null) {
            return loeExpression.or(goeExpression);
        } else {
            return loeExpression;
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
