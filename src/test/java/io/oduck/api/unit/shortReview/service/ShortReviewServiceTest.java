package io.oduck.api.unit.shortReview.service;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.ShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.Sort;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewCountRes;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewRes;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.domain.review.service.ShortReviewServiceImpl;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.stub.MemberStub;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import static io.oduck.api.global.utils.AnimeTestUtils.createAnime;
import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;
import static io.oduck.api.global.utils.ShortReviewTestUtils.createPatchShortReview;
import static io.oduck.api.global.utils.ShortReviewTestUtils.createPostShoreReviewReq;
import static io.oduck.api.global.utils.ShortReviewTestUtils.createShortReview;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShortReviewServiceTest {

    @InjectMocks
    private ShortReviewServiceImpl shortReviewService;

    @Mock
    private ShortReviewRepository shortReviewRepository;

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("짧은 리뷰 작성")
    class PostShortReview{

        @Test
        @DisplayName("짧은 리뷰 작성 성공")
        void postShortReview(){
            //given
            Long animeId = 1L;
            Long memberId = 1L;
            ShortReviewReq shortReviewReq = createPostShoreReviewReq();
            ShortReview shortReview = createShortReview();

            Anime anime = createAnime();
            given(animeRepository.findByIdForUpdate(animeId)).willReturn(Optional.of(anime));

            Member member = new MemberStub().getMember();
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            //when
            shortReviewService.save(memberId,shortReviewReq);

            //then
            verify(animeRepository,times(1)).findByIdForUpdate(any());
            verify(memberRepository,times(1)).findById(any());
        }
    }

    @Nested
    @DisplayName("짧은 리뷰 조회")
    class GetShortReviews{

        @Test
        @DisplayName("짧은 리뷰 조회")
        void getShortReviews(){
            //given
            Long animeId = 1L;
            String cursor = null;
            Sort sort = Sort.CREATED_AT;
            OrderDirection order = OrderDirection.ASC;
            int size = 10;

            List<ShortReviewDsl> sampleShortReviews = new ArrayList<>();

            Slice<ShortReviewDsl> sampleSlice = new SliceImpl<>(sampleShortReviews);

            //when
            when(shortReviewRepository.selectShortReviews(
                animeId,
                cursor,
                applyPageableForNonOffset(size, sort.getSort(), order.getOrder())
            )).thenReturn(sampleSlice);

            SliceResponse<ShortReviewRes> result = shortReviewService.getShortReviews(animeId,cursor,sort,order,size);

            //then
            assertEquals(sampleSlice.getSize(), result.getItems().size());
            assertNotNull(result.getCursor());
            assertFalse(result.isHasNext());
        }
    }

    @Nested
    @DisplayName("짧은 리뷰 수정")
    class PatchShortReview{

        ShortReview shortReview = createShortReview();

        @Test
        @DisplayName("짧은 리뷰 수정 성공")
        void patchShortReview(){
            //given
            Long reviewId = 1L;
            Long memberId = 1L;
            ShortReviewReq patchShortReviewReq = createPatchShortReview();

            given(shortReviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(shortReview));

            //when
            shortReviewService.update(memberId, reviewId, patchShortReviewReq);

            //then
            verify(shortReviewRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("회원 ID로 짧은 리뷰 갯수 조회")
    @Nested
    class GetShortReviewCountByMemberId {

        @Test
        @DisplayName("회원 ID로 짧은 리뷰 갯수 조회 성공")
        void getShortReviewCountByMemberId() {
            //given
            Long memberId = 1L;
            Long count = 1L;

            given(shortReviewRepository.countByMemberId(memberId)).willReturn(count);

            //when
            ShortReviewCountRes result = shortReviewService.getShortReviewCountByMemberId(memberId);

            //then
            assertEquals(count, result.getCount());
        }
    }
}