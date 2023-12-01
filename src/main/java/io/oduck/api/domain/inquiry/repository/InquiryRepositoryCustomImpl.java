package io.oduck.api.domain.inquiry.repository;

import static io.oduck.api.domain.inquiry.entity.QInquiry.inquiry;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.inquiry.dto.InquiryRes.MyInquiry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InquiryRepositoryCustomImpl implements InquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MyInquiry> getAllByMemberId(Long memberId, Pageable pageable) {

        List<MyInquiry> content = queryFactory.select(
                Projections.constructor(
                    MyInquiry.class,
                    inquiry.id,
                    inquiry.title,
                    inquiry.createdAt,
                    inquiry.answer
                )
            ).from(inquiry)
            .where(memberIdEq(memberId))
            .fetch();

        JPAQuery<Long> expression = queryFactory.select(inquiry.id.count()).from(inquiry)
            .where(memberIdEq(memberId));

        return PageableExecutionUtils.getPage(content, pageable, expression::fetchOne);
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId == null ? null : inquiry.member.id.eq(memberId);
    }
}
