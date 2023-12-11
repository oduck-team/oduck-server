package io.oduck.api.unit.contact.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.oduck.api.domain.contact.entity.Contact;
import io.oduck.api.domain.contact.service.ContactPolicy;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.global.exception.ForbiddenException;
import org.junit.jupiter.api.Test;

public class ContactPolicyTest {

    private final ContactPolicy contactPolicy = new ContactPolicy();

    @Test
    void 질문한_클라이언트와_조회하려는_클라이언트가_다르면_예외가_터진다() {
        //given
        Member writer = Member.builder().id(1L).build();

        Contact contact = Contact.builder().customer(writer).build();

        Member target = Member.builder().id(2L).build();

        //when
        //then
        assertThatThrownBy(() -> contactPolicy.isAccessOwnInquiry(contact, target)).isInstanceOf(
            ForbiddenException.class);
    }
}
