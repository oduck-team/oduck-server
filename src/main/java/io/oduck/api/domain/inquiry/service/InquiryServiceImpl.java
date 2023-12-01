package io.oduck.api.domain.inquiry.service;

import static io.oduck.api.domain.inquiry.dto.InquiryReq.PostReq;
import static io.oduck.api.domain.inquiry.dto.InquiryRes.MyInquiry;

import io.oduck.api.domain.inquiry.dto.CheckAnswerRequest;
import io.oduck.api.domain.inquiry.dto.InquiryFeedback;
import io.oduck.api.domain.inquiry.dto.InquiryRequestHolder;
import io.oduck.api.domain.inquiry.dto.InquiryRes.DetailRes;
import io.oduck.api.domain.inquiry.entity.FeedbackType;
import io.oduck.api.domain.inquiry.entity.Inquiry;
import io.oduck.api.domain.inquiry.repository.InquiryRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.common.PageResponse;
import io.oduck.api.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService{

    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;

    private final InquiryPolicy inquiryPolicy;

    @Override
    public void inquiry(Long memberId, PostReq request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        member.inquiry(InquiryRequestHolder.from(request, member));
    }

    @Override
    public PageResponse<MyInquiry> getAllByMemberId(Long memberId, int page, int size) {
        Page<MyInquiry> myInquiries = inquiryRepository.getAllByMemberId(memberId,
            PageRequest.of(page, size));
        return PageResponse.of(myInquiries);
    }

    @Override
    public DetailRes getByMemberId(Long id, Long memberId) {
        Inquiry inquiry = inquiryRepository.findWithMemberById(id)
            .orElseThrow(() -> new NotFoundException("inquiry"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        inquiryPolicy.isAccessOwnInquiry(inquiry, member);

        member.checkAnswer(CheckAnswerRequest.from(inquiry.getId()));
        return DetailRes.from(inquiry);
    }

    @Override
    public boolean hasNotCheckedAnswer(Long id, Long memberId) {
        Inquiry inquiry = inquiryRepository.findWithMemberById(id)
            .orElseThrow(() -> new NotFoundException("inquiry"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        inquiryPolicy.isAccessOwnInquiry(inquiry, member);

        return inquiryRepository.existsByIdAndCheck(id, false);
    }

    @Override
    public void feedbackAnswer(Long id, Long memberId, FeedbackType helpful) {
        Inquiry inquiry = inquiryRepository.findWithMemberById(id)
            .orElseThrow(() -> new NotFoundException("inquiry"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("member"));

        inquiryPolicy.isAccessOwnInquiry(inquiry, member);

        member.feedbackInquiry(InquiryFeedback.from(id, helpful));
    }

//    @Override
//    public Page<?> getAll() {
//        return null;
//    }
//
//    @Override
//    public void answer() {
//
//    }
//
//    @Override
//    public void update(Long id) {
//
//    }
}
