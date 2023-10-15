package io.oduck.api.domain.member.repository;

import static io.oduck.api.domain.member.entity.QMemberProfile.memberProfile;
import static io.oduck.api.domain.review.entity.QShortReview.shortReview;
import static io.oduck.api.domain.reviewLike.entity.QShortReviewLike.shortReviewLike;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;
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
    public Long countLikesByMemberId(Long id) {
        Long likesCount = query
            .select(shortReviewLike.id.count())
            .from(shortReview)
            .join(shortReviewLike).on(shortReview.id.eq(shortReviewLike.shortReview.id))
            .where(shortReview.member.id.eq(id))
            .fetchOne();
        return likesCount;
    }
}
