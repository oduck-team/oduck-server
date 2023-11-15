package io.oduck.api.domain.studio.repository;

import static io.oduck.api.domain.studio.entity.QStudio.studio;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudioRepositoryCustomImpl implements StudioRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByName(String name) {
        Integer fetchOne = queryFactory.selectOne()
            .from(studio)
            .where(
                studio.name.eq(name),
                notDeleted()
            )
            .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression notDeleted() {
        return studio.deletedAt.isNull();
    }
}
