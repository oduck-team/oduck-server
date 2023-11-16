package io.oduck.api.domain.series.repository;

import static io.oduck.api.domain.series.entity.QSeries.series;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeriesRepositoryCustomImpl implements SeriesRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByTitle(String title) {
        Integer fetchOne = queryFactory.selectOne()
            .from(series)
            .where(
                series.title.eq(title),
                notDeleted()
            )
            .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression notDeleted() {
        return series.deletedAt.isNull();
    }
}
