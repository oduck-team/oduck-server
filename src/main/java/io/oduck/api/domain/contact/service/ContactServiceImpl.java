package io.oduck.api.domain.contact.service;

import static io.oduck.api.domain.contact.dto.ContactReq.PostReq;
import static io.oduck.api.domain.contact.dto.ContactRes.MyInquiry;

import io.oduck.api.domain.admin.entity.Admin;
import io.oduck.api.domain.admin.repository.AdminRepository;
import io.oduck.api.domain.contact.dto.AnswerFeedback;
import io.oduck.api.domain.contact.dto.ContactId;
import io.oduck.api.domain.contact.infra.event.ContactEvent;
import io.oduck.api.domain.contact.dto.ContactReq.AnswerReq;
import io.oduck.api.domain.contact.dto.ContactReq.AnswerUpdateReq;
import io.oduck.api.domain.contact.dto.ContactRequestHolder;
import io.oduck.api.domain.contact.dto.ContactRes.DetailRes;
import io.oduck.api.domain.contact.entity.Contact;
import io.oduck.api.domain.contact.entity.FeedbackType;
import io.oduck.api.domain.contact.infra.event.ContactEventPublisher;
import io.oduck.api.domain.contact.repository.ContactRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.common.PageResponse;
import io.oduck.api.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final MemberRepository memberRepository;
    private final ContactRepository contactRepository;
    private final AdminRepository adminRepository;

    private final ContactPolicy contactPolicy;
    private final ContactEventPublisher eventPublisher;


    @Override
    @Transactional
    public void contact(Long memberId, PostReq request) {
        Member member = memberRepository.findWithProfileById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        member.contact(ContactRequestHolder.from(request, member));

        String nickname = member.getMemberProfile().getName();
        eventPublisher.contact(ContactEvent.from(request, nickname));
    }

    @Override
    public PageResponse<MyInquiry> getAllByMemberId(Long memberId, int page, int size) {
        Page<MyInquiry> myInquiries = contactRepository.getAllByMemberId(memberId,
            PageRequest.of(page, size));
        return PageResponse.of(myInquiries);
    }

    @Override
    public DetailRes getByMemberId(Long id, Long memberId) {
        Contact contact = contactRepository.findWithMemberById(id)
            .orElseThrow(() -> new NotFoundException("inquiry"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        contactPolicy.isAccessOwnInquiry(contact, member);

        member.checkAnswer(ContactId.from(contact.getId()));
        return DetailRes.from(contact);
    }

    @Override
    @Transactional
    public boolean hasNotCheckedAnswer(Long id, Long memberId) {
        Contact contact = contactRepository.findWithMemberById(id)
            .orElseThrow(() -> new NotFoundException("inquiry"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        contactPolicy.isAccessOwnInquiry(contact, member);

        return contactRepository.existsByIdAndCheck(id, false);
    }

    @Override
    @Transactional
    public void feedbackAnswer(Long id, Long memberId, FeedbackType helpful) {
        Contact contact = contactRepository.findWithMemberById(id)
            .orElseThrow(() -> new NotFoundException("inquiry"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        contactPolicy.isAccessOwnInquiry(contact, member);

        member.feedbackAnswer(AnswerFeedback.from(id, helpful));
    }

//    @Override
//    public Page<?> getAll() {
//        return null;
//    }

    @Override
    @Transactional
    public void answer(Long id, Long adminId, AnswerReq request) {
        Contact contact = contactRepository.findWithMemberById(id)
            .orElseThrow(() -> new NotFoundException("inquiry"));

        Admin admin = adminRepository.findWithAnswerById(adminId)
            .orElseThrow(() -> new NotFoundException("admin"));

        admin.answer(AnswerHolder.from(contact, request));
    }

    @Override
    @Transactional
    public void update(Long answerId, Long adminId, AnswerUpdateReq request) {
        Admin admin = adminRepository.findWithAnswerById(adminId)
            .orElseThrow(() -> new NotFoundException("admin"));

        admin.updateAnswer(AnswerUpdateHolder.from(answerId, request));
    }
}
