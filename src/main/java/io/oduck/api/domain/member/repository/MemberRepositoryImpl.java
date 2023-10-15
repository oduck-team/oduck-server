package io.oduck.api.domain.member.repository;

import static io.oduck.api.domain.member.entity.QMemberProfile.memberProfile;
import static io.oduck.api.domain.review.entity.QShortReview.shortReview;
import static io.oduck.api.domain.reviewLike.entity.QShortReviewLike.shortReviewLike;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.member.dto.MemberDslDto.MemberActivity;
import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.entity.QMemberProfile;
import io.oduck.api.domain.review.entity.QShortReview;
import io.oduck.api.global.utils.QueryDslUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory query;
//    private final QueryDslUtils queryDslUtils;


    @Override
    public ProfileWithoutActivity selectProfileByName(String name) {
        ProfileWithoutActivity memberProfileRes = query
            .select(
                Projections.constructor(
                    ProfileWithoutActivity.class,
                    memberProfile.member.id,
                    memberProfile.name,
                    memberProfile.info,
                    memberProfile.thumbnail,
                    memberProfile.backgroundImage,
                    memberProfile.point
                )
            )
            .from(memberProfile)
            .where(memberProfile.name.eq(name))
            .fetchOne();
        return memberProfileRes;
    }

    @Override
    public MemberActivity selectActivityByMemberId(Long id) {
        NumberExpression<Long> reviewCount = shortReview.id.count();
        NumberExpression<Long> reviewLikeCount = shortReviewLike.id.count();

        MemberActivity activity = (MemberActivity) query
            .select(reviewCount.as("reviews"), reviewLikeCount.sum().as("likes"))
            .from(shortReview)
            .join(shortReviewLike).on(shortReviewLike.shortReview.id.eq(shortReview.id))
            .where(shortReview.member.id.eq(id))
            .groupBy(shortReview.id)
            .fetchOne();
        return activity;
    }
}
