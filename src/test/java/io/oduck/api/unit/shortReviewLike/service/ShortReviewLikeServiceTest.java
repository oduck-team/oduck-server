package io.oduck.api.unit.shortReviewLike.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeReqDto.ShortReviewLikeReq;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeResDto.IsLikeRes;
import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import io.oduck.api.domain.reviewLike.repository.ShortReviewLikeRepository;
import io.oduck.api.domain.reviewLike.service.ShortReviewLikeServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShortReviewLikeServiceTest {
    @InjectMocks
    private ShortReviewLikeServiceImpl shortReviewLikeService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ShortReviewRepository shortReviewRepository;

    @Mock
    private ShortReviewLikeRepository shortReviewLikeRepository;

        Member member = Member
                            .builder()
                            .id(1L)
                            .build();

        ShortReview shortReview = ShortReview
                                      .builder()
                                      .id(1L)
                                      .build();

       ShortReviewLike shortReviewLike = ShortReviewLike
                                             .builder()
                                             .member(member)
                                             .shortReview(shortReview)
                                             .build();

       ShortReviewLikeReq req = ShortReviewLikeReq
                                    .builder()
                                    .shortReviewId(1L)
                                    .build();

    @Nested
    @DisplayName("짧은 리뷰 좋아요")
    class PostLike{

        @Test
        @DisplayName("짧은 리뷰 좋아요 생성 성공")
        void shortReviewLikeSaveSuccess() {
            //given
            given(shortReviewLikeRepository.findByMemberIdAndShortReviewId(anyLong(),anyLong()))
                .willReturn(Optional.empty());
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(shortReviewRepository.findById(anyLong())).willReturn(Optional.of(shortReview));

            boolean result = shortReviewLikeService.postLike(member.getId(), req);

            assertDoesNotThrow(() -> shortReviewLikeService.postLike(member.getId(), req));
            assertTrue(result);
        }

        @Test
        @DisplayName("짧은 리뷰 좋아요 삭제 성공")
        void shortReviewLikeDeleteSuccess(){
            //given(shortReviewLikeRepository.save(shortReviewLike));
            given(shortReviewLikeRepository.findByMemberIdAndShortReviewId(anyLong(), anyLong()))
                .willReturn(Optional.of(shortReviewLike));

            shortReviewLikeRepository.delete(shortReviewLike);
            boolean result = shortReviewLikeService.postLike(member.getId(), req);

            assertDoesNotThrow(() -> shortReviewLikeService.postLike(member.getId(), req));
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("짧은 리뷰 존재 유무")
    class CheckLike{

        @Test
        @DisplayName("짧은 리뷰 좋아요 존재 시 true")
        void checkShortReviewLikeTrue(){
            given(shortReviewLikeRepository.findByMemberIdAndShortReviewId(member.getId(), shortReview.getId())).willReturn(Optional.of(shortReviewLike));

            IsLikeRes hasLikeRes = shortReviewLikeService.checkReviewLike(shortReview.getId(), member.getId());

            assertNotNull(hasLikeRes);
            assertTrue(hasLikeRes.getIsLike());
        }

        @Test
        @DisplayName("짧은 리뷰 좋아요 없을 시 false")
        void checkShortReviewLikeFalse(){
            given(shortReviewLikeRepository.findByMemberIdAndShortReviewId(member.getId(), shortReview.getId())).willReturn(Optional.empty());

            IsLikeRes hasLikeRes = shortReviewLikeService.checkReviewLike(shortReview.getId(), member.getId());

            assertNotNull(hasLikeRes);
            assertFalse(hasLikeRes.getIsLike());
        }
    }
}
