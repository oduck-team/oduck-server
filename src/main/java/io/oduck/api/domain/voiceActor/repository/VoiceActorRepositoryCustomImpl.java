package io.oduck.api.domain.voiceActor.repository;

import static io.oduck.api.domain.voiceActor.entity.QVoiceActor.voiceActor;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoiceActorRepositoryCustomImpl implements VoiceActorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByName(String name) {
        Integer fetchOne = queryFactory.selectOne()
            .from(voiceActor)
            .where(
                voiceActor.name.eq(name),
                notDeleted()
            )
            .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression notDeleted() {
        return voiceActor.deletedAt.isNull();
    }
}
