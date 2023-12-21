package io.oduck.api.domain.genre.repository;

import static io.oduck.api.domain.anime.entity.QAnimeGenre.animeGenre;
import static io.oduck.api.domain.genre.entity.QGenre.genre;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.anime.entity.QAnimeGenre;
import io.oduck.api.domain.genre.entity.Genre;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Genre> findAllByAnimeId(Long animeId) {
        JPAQuery<Genre> genreJPAQuery = queryFactory.select(genre)
            .from(genre)
            .join(animeGenre).on(genre.id.eq(animeGenre.genre.id))
            .where(
                animeGenre.anime.id.eq(animeId),
                genre.deletedAt.isNull()
            )
            .limit(3);
        return genreJPAQuery.fetch();
    }

    @Override
    public boolean existsByName(String name) {
        Integer fetchOne = queryFactory
            .selectOne()
            .from(genre)
            .where(
                genre.name.eq(name),
                notDeleted()
            ).fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression notDeleted() {
        return genre.deletedAt.isNull();
    }
}
