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
import io.oduck.api.domain.admin.dto.AdminReq.QueryType;
import io.oduck.api.domain.admin.dto.AdminReq.SearchFilter;
import io.oduck.api.domain.admin.dto.AdminRes;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.QAnime;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Status;
import io.oduck.api.global.common.PageResponse;
import io.oduck.api.global.utils.QueryDslUtils;
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

    @Override
    public PageResponse<AdminRes.SearchResult> findPageByCondition(String query,
        QueryType queryType, Pageable pageable, SearchFilter searchFilter) {
        JPAQuery<AdminRes.SearchResult> jpaQuery = queryFactory
            .select(
                Projections.constructor(
                    AdminRes.SearchResult.class,
                    anime.id,
                    anime.title,
                    anime.thumbnail,
                    anime.year,
                    anime.quarter,
                    anime.isReleased,
                    anime.status,
                    anime.createdAt,
                    anime.series.id,
                    anime.series.title,
                    anime.bookmarkCount,
                    anime.starRatingScoreTotal,
                    anime.starRatingCount,
                    anime.reviewCount,
                    anime.viewCount
                )
            )
            .from(anime)
            .leftJoin(anime.series)
            .where(
                queryEq(query, queryType),
                isReleased(searchFilter.getIsReleased()),
                yearsIn(searchFilter.getYears()),
                statusIn(searchFilter.getStatuses()),
                notDeleted()
            );

        //TODO: 페이징 요청마다 total 쿼리를 구함 -> 디비 부하
        Long total = queryFactory
            .select(
                anime.count()
            )
            .from(anime)
            .where(
                queryEq(query, queryType),
                isReleased(searchFilter.getIsReleased()),
                yearsIn(searchFilter.getYears()),
                statusIn(searchFilter.getStatuses()),
                notDeleted()
            )
            .fetchOne();

        return PageResponse.of(
            QueryDslUtils.fetchPage(
                sortPath(anime), jpaQuery, total, pageable
            )
        );
    }

    private BooleanExpression queryEq(String query, QueryType queryType) {
        if(StringUtils.isNullOrEmpty(query)) {
            return null;
        }

        if(queryType == null) {
            queryType = QueryType.TITLE;
        }

        // 쿼리 타입 계산
        if(queryType == QueryType.TITLE) {
            return titleEq(query);
        } else if(queryType == QueryType.SERIES) {
            return anime.series.title.contains(query);
        } else {
            // 그 외 모두 아이디 검색으로 로직 수행
            try {
                return anime.id.eq(Long.parseLong(query));
                // 숫자가 아닌 문자열이 올 경우 null 반환
            } catch (NumberFormatException e) {
                return null;
            }
        }
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

    private BooleanExpression isReleased(Boolean isReleased) {

        return isReleased == null ? null : anime.isReleased.eq(isReleased);
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

        if(isParsableAsLong(cursor) == false) {
            return null;
        }

        Long id = Long.parseLong(cursor);

        List<Sort.Order> orders = pageable.getSort().get().collect(Collectors.toList());
        Direction direction = orders.get(0).getDirection();

        if (direction == Direction.ASC) {
            return anime.id.gt(id);
        }else{
            return anime.id.lt(id);
        }
    }

    private boolean isParsableAsLong(String cursor) {
        try {
            Long.parseLong(cursor);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
