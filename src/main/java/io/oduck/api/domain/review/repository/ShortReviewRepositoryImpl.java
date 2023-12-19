package io.oduck.api.domain.review.repository;

import static io.oduck.api.domain.anime.entity.QAnime.anime;
import static io.oduck.api.domain.member.entity.QMember.member;
import static io.oduck.api.domain.review.entity.QShortReview.shortReview;
import static io.oduck.api.domain.reviewLike.entity.QShortReviewLike.shortReviewLike;
import static io.oduck.api.domain.starRating.entity.QStarRating.starRating;

import static io.oduck.api.global.utils.QueryDslUtils.fetchSliceByCursor;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDslWithTitle;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ShortReviewRepositoryImpl implements ShortReviewRepositoryCustom {

    private final JPAQueryFactory query;


    @Override
    public Slice<ShortReviewDsl> selectShortReviews(Long animeId, String cursor,
        Pageable pageable) {
        String property = pageable.getSort().get().toList().get(0).getProperty();
        JPAQuery<ShortReviewDsl> shortReviews = query
            .select(
                Projections.constructor(
                    ShortReviewDsl.class,
                    shortReview.id,
                    anime.id,
                    member.memberProfile.name,
                    member.memberProfile.thumbnail,
                    starRating.score,
                    shortReview.content,
                    shortReview.hasSpoiler,
                    shortReviewLike.id.count().as("likeCount"),
                    shortReview.createdAt
                )
            )
            .from(shortReview)
            .join(member).on(member.id.eq(shortReview.member.id))
            .join(anime).on(anime.id.eq(shortReview.anime.id))
            .leftJoin(shortReviewLike).on(shortReview.id.eq(shortReviewLike.shortReview.id))
            .join(starRating).on(starRating.anime.id.eq(shortReview.anime.id)
                .and(starRating.member.id.eq(shortReview.member.id)))
            .where(anime.id.eq(animeId).and(shortReview.deletedAt.isNull()))
            .groupBy(shortReview.id, member.id)
            .having(cursorCondition(cursor, pageable))
            .limit(pageable.getPageSize());

        return fetchSliceByCursor(sortPath(property), shortReviews, pageable);
    }

    @Override
    public Slice<ShortReviewDslWithTitle> selectShortReviewsByMemberId(Long memberId, String cursor,
        Pageable pageable) {
        String property = pageable.getSort().get().toList().get(0).getProperty();
        JPAQuery<ShortReviewDslWithTitle> shortReviews = query
            .select(
                Projections.constructor(
                    ShortReviewDslWithTitle.class,
                    shortReview.id,
                    anime.id,
                    anime.title,
                    anime.thumbnail,
                    starRating.score,
                    shortReview.content,
                    shortReview.hasSpoiler,
                    shortReviewLike.id.count().as("likeCount"),
                    shortReview.createdAt
                )
            )
            .from(shortReview)
            .join(anime).on(anime.id.eq(shortReview.anime.id))
            .leftJoin(starRating).on(starRating.anime.id.eq(shortReview.anime.id)
                .and(starRating.member.id.eq(shortReview.member.id)))
            .leftJoin(shortReviewLike).on(shortReview.id.eq(shortReviewLike.shortReview.id))
            .where(shortReview.member.id.eq(memberId).and(shortReview.deletedAt.isNull()))
            .groupBy(shortReview.id)
            .having(cursorCondition(cursor, pageable))
            .limit(pageable.getPageSize());

        return fetchSliceByCursor(sortPath(property), shortReviews, pageable);
    }

    private BooleanExpression cursorCondition(String cursor, Pageable pageable) {
        if (cursor == null) {
            return null;
        }

        List<Order> orderList = pageable.getSort().get().toList();
        Direction direction = orderList.get(0).getDirection();
        String property = orderList.get(0).getProperty();

        switch (property) {

            case "likeCount":
                String[] likeCountAndCreateAt = cursor.split(", ");
                int likeCount = Integer.parseInt(likeCountAndCreateAt[0]);
                LocalDateTime likeCountCreateAt = LocalDateTime.parse(likeCountAndCreateAt[1]);

                if (direction == Direction.ASC) {
                    return shortReviewLike.id.count().gt(likeCount)
                        .or(shortReviewLike.id.count().goe(likeCount).and(shortReview.createdAt.lt(
                            likeCountCreateAt)))//조회할 좋아요가 크거나 같으면, 첫 커서의 날짜가 크면
                        .or(shortReviewLike.id.count().isNull()
                            .and(shortReview.createdAt.lt(likeCountCreateAt)));
                } else {
                    return shortReviewLike.id.count().lt(likeCount)
                        .or(shortReviewLike.id.count().loe(likeCount).and(shortReview.createdAt.lt(
                            likeCountCreateAt)))//조회할 좋아요가 크거나 같으면, 첫 커서의 날짜가 크면
                        .or(shortReviewLike.id.count().isNull()
                            .and(shortReview.createdAt.lt(likeCountCreateAt)));
                }
            case "score":
                String[] scoreAndCreateAt = cursor.split(", ");
                int score = Integer.parseInt(scoreAndCreateAt[0]);
                LocalDateTime scoreCreateAt = LocalDateTime.parse(scoreAndCreateAt[1]);//커서의 날짜

                if (direction == Direction.ASC) {
                    return starRating.score.gt(score)
                        .or(starRating.score.goe(score)
                            .and(shortReview.createdAt.lt(scoreCreateAt)));
                } else {
                    return starRating.score.lt(score)
                        .or(starRating.score.loe(score)
                            .and(shortReview.createdAt.lt(scoreCreateAt)));
                }
            default:
                if (direction == Direction.ASC) {
                    return shortReview.createdAt.gt(LocalDateTime.parse(cursor));
                } else {
                    return shortReview.createdAt.lt(LocalDateTime.parse(cursor));
                }
        }
    }

    private List<Path> sortPath(String property) {
        List<Path> path = new ArrayList<>();
        switch (property) {
            case "likeCount":
                path.add(shortReviewLike);
                path.add(shortReview);
            case "score":
                path.add(starRating);
                path.add(shortReview);
            default:
                path.add(shortReview);
        }
        return path;
    }
}


