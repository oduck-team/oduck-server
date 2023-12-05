package io.oduck.api.domain.attractionPoint.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static io.oduck.api.domain.attractionPoint.entity.QAttractionPoint.attractionPoint;


@Slf4j
@Repository
@RequiredArgsConstructor
class AttractionPointRepositoryImpl implements AttractionPointRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public Long countElementByAnimeId(AttractionElement attractionElement, Long animeId) {
        Long elementCount = query
            .select(attractionPoint.attractionElement.count())
            .from(attractionPoint)
            .where(attractionPoint.attractionElement.eq(attractionElement)
                    .and(attractionPoint.anime.id.eq(animeId)))
            .fetchOne();
        return elementCount;
    }

}
