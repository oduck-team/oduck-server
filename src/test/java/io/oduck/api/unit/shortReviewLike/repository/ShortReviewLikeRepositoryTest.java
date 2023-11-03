package io.oduck.api.unit.shortReviewLike.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import io.oduck.api.domain.reviewLike.repository.ShortReviewLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ShortReviewLikeRepositoryTest {

    @Autowired
    private ShortReviewLikeRepository shortReviewLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ShortReviewRepository shortReviewRepository;

    Member member = Member
                        .builder()
                        .id(2L)
                        .build();
    ShortReview shortReview = ShortReview
                                  .builder()
                                  .id(1L)
                                  .build();

    @Nested
    @DisplayName("짧은 리뷰 좋아요 저장, 삭제")
    class PostLike{

        @Test
        @DisplayName("짧은 리뷰 좋아요 저장 성공")
        void shortReviewLikeSaveSuccess() {
            //given
            ShortReviewLike shortReviewLike = ShortReviewLike
                                                  .builder()
                                                  .member(member)
                                                  .shortReview(shortReview)
                                                  .build();

            ShortReviewLike findLike = shortReviewLikeRepository.findByMemberIdAndShortReviewId(member.getId(), shortReview.getId()).orElse(null);
            ShortReviewLike saveLike = shortReviewLikeRepository.save(shortReviewLike);

            assertNull(findLike);
            assertEquals(member.getId(), saveLike.getMember().getId());
            assertEquals(shortReview.getId(), saveLike.getShortReview().getId());
        }

        @Test
        @DisplayName("짧은 리뷰 좋아요 삭제 성공")
        void shortReviewLikeDeleteSuccess(){
            //given
            ShortReviewLike shortReviewLike = ShortReviewLike
                                                  .builder()
                                                  .member(member)
                                                  .shortReview(shortReview)
                                                  .build();

            ShortReviewLike saveLike = shortReviewLikeRepository.save(shortReviewLike);

            //when
            ShortReviewLike findLike = shortReviewLikeRepository.findByMemberIdAndShortReviewId(member.getId(), shortReview.getId()).orElse(shortReviewLike);
            shortReviewLikeRepository.delete(findLike);

            //then
            ShortReviewLike deleteLike = shortReviewLikeRepository.findByMemberIdAndShortReviewId(member.getId(), shortReview.getId()).orElse(null);
            assertNotNull(findLike);
            assertThat(deleteLike).isNull();
        }
    }

    @Nested
    @DisplayName("짧은 리뷰 존재 유무")
    class CheckLike{

        @Test
        @DisplayName("짧은 리뷰 좋아요 존재 시 true")
        void checkShortReviewLikeTrue(){
            //given
            ShortReviewLike shortReviewLike = ShortReviewLike
                                                  .builder()
                                                  .member(member)
                                                  .shortReview(shortReview)
                                                  .build();

            ShortReviewLike saveLike = shortReviewLikeRepository.save(shortReviewLike);

            //when
            boolean findLike = shortReviewLikeRepository.findByMemberIdAndShortReviewId(member.getId(), shortReview.getId()).isPresent();

            //then
            assertTrue(findLike);
        }

        @Test
        @DisplayName("짧은 리뷰 좋아요 없을 시 false")
        void checkShortReviewLikeFalse(){
            //given
            ShortReviewLike shortReviewLike = ShortReviewLike
                                                  .builder()
                                                  .member(member)
                                                  .shortReview(shortReview)
                                                  .build();

            //when
            boolean findLike = shortReviewLikeRepository.findByMemberIdAndShortReviewId(member.getId(), shortReview.getId()).isPresent();

            //then
            assertFalse(findLike);
        }
    }
}
