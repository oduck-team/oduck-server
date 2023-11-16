package io.oduck.api.domain.originalAuthor.repository;


import static io.oduck.api.domain.originalAuthor.entity.QOriginalAuthor.originalAuthor;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OriginalAuthorRepositoryCustomImpl implements OriginalAuthorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByName(String name) {
        Integer fetchOne = queryFactory.selectOne()
            .from(originalAuthor)
            .where(
                originalAuthor.name.eq(name),
                notDeleted()
            )
            .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression notDeleted() {
        return originalAuthor.deletedAt.isNull();
    }
}
