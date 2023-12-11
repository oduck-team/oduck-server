package io.oduck.api.domain.inquiry.repository;

import static io.oduck.api.domain.inquiry.entity.QAnswer.answer;
import static io.oduck.api.domain.inquiry.entity.QContact.contact;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oduck.api.domain.inquiry.dto.ContactRes.MyInquiry;
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
public class ContactRepositoryCustomImpl implements ContactRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MyInquiry> getAllByMemberId(Long memberId, Pageable pageable) {

        List<MyInquiry> content = queryFactory.select(
                Projections.constructor(
                    MyInquiry.class,
                    contact.id,
                    contact.title,
                    contact.createdAt,
                    contact.answer
                )
            ).from(contact)
            .where(memberIdEq(memberId))
            .fetch();

        JPAQuery<Long> expression = queryFactory.select(contact.id.count()).from(contact)
            .where(memberIdEq(memberId));

        return PageableExecutionUtils.getPage(content, pageable, expression::fetchOne);
    }

    public boolean existsByIdAndCheck(Long id, Boolean check) {
        Integer fetchOne = queryFactory.selectOne()
            .from(contact)
            .join(contact.answer, answer)
            .where(
                idEq(id),
                isCheck(check)
            )
            .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression isCheck(Boolean check) {
        return check == null ? null : answer.check.eq(check);
    }

    private BooleanExpression idEq(Long id) {
        return id == null? null : answer.id.eq(id);
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId == null ? null : contact.customer.id.eq(memberId);
    }
}
