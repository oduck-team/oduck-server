package io.oduck.api.domain.bookmark.repository;

import static io.oduck.api.domain.anime.entity.QAnime.anime;
import static io.oduck.api.domain.bookmark.entity.QBookmark.bookmark;
import static io.oduck.api.domain.member.entity.QMember.member;
import static io.oduck.api.domain.starRating.entity.QStarRating.starRating;
import static io.oduck.api.global.utils.QueryDslUtils.fetchSliceByCursor;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom{
    private final JPAQueryFactory query;

    @Override
    public Slice<BookmarkDsl> selectBookmarks(Long memberId, String cursor, Pageable pageable) {
        String property = pageable.getSort().get().toList().get(0).getProperty();
        JPAQuery<BookmarkDsl> bookmarks = query
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
            .where(cursorCondition(cursor, pageable))
            .limit(pageable.getPageSize());

        return fetchSliceByCursor(sortPath(property), bookmarks, pageable);
    }

    private Path sortPath(String property) {
        switch (property) {
            case "score":
                return starRating;
            case "title":
                return anime;
            default:
                return bookmark;
        }
    }

    private BooleanExpression cursorCondition(String cursor, Pageable pageable) {
        if (cursor == null) {
            return null;
        }

        List<Order> orderList = pageable.getSort().get().toList();
        Direction direction = orderList.get(0).getDirection();
//        String property = orderList.get(0).getProperty();

        if (direction == Direction.ASC) {
            return bookmark.id.gt(Long.parseLong(cursor));
        } else {
            return bookmark.id.lt(Long.parseLong(cursor));
        }
    }
}
