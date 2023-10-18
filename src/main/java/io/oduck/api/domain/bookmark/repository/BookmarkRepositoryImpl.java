package io.oduck.api.domain.bookmark.repository;

import static io.oduck.api.domain.anime.entity.QAnime.anime;
import static io.oduck.api.domain.bookmark.entity.QBookmark.bookmark;
import static io.oduck.api.domain.member.entity.QMember.member;
import static io.oduck.api.domain.starRating.entity.QStarRating.starRating;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom{
    private final JPAQueryFactory query;
//    private final QueryDslUtils queryDslUtils;

    @Override
    public List<BookmarkDsl> selectBookmarks(Long memberId) {
        List<BookmarkDsl> bookmarks = query
            .select(
                Projections.constructor(
                    BookmarkDsl.class,
                    anime.id,
                    anime.title,
                    anime.thumbnail,
                    starRating.score,
                    bookmark.createdAt
                )
            )
            .from(bookmark)
            .join(member).on(member.id.eq(bookmark.member.id))
            .join(anime).on(bookmark.anime.id.eq(anime.id))
            .leftJoin(starRating).on(member.id.eq(starRating.member.id).and(anime.id.eq(starRating.anime.id)))
            .where(member.id.eq(memberId))
            .fetch();

        return bookmarks;
    }
}
