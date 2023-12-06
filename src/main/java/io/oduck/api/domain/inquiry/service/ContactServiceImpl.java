package io.oduck.api.domain.inquiry.service;

import static io.oduck.api.domain.inquiry.dto.ContactReq.PostReq;
import static io.oduck.api.domain.inquiry.dto.ContactRes.MyInquiry;

import io.oduck.api.domain.inquiry.dto.AnswerFeedback;
import io.oduck.api.domain.inquiry.dto.ContactId;
import io.oduck.api.domain.inquiry.dto.ContactRequestHolder;
import io.oduck.api.domain.inquiry.dto.ContactRes.DetailRes;
import io.oduck.api.domain.inquiry.entity.Contact;
import io.oduck.api.domain.inquiry.entity.FeedbackType;
import io.oduck.api.domain.inquiry.repository.ContactRepository;
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

    private final ContactPolicy contactPolicy;

    @Override
    @Transactional
    public void inquiry(Long memberId, PostReq request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        member.inquiry(ContactRequestHolder.from(request, member));
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

//    @Override
//    @Transactional
//    public void answer(Long adminId, AnswerReq request) {
//    }

//    @Override
//    @Transactional
//    public void update(Long id) {
//
//    }
}
