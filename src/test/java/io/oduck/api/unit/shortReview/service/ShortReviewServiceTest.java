package io.oduck.api.unit.shortReview.service;

import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.service.ShortReviewServiceStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ShortReviewServiceTest {

    @InjectMocks
    private ShortReviewServiceStub shortReviewService;

    @Nested
    @DisplayName("짧은 리뷰 조회")
    class GetShortReviews{

        @Test
        @DisplayName("짧은 리뷰 조회")
        void getShortReviews(){
            //given
            Long animeId = 1L;

            //when
            ShortReviewResDto response = shortReviewService.getShortReviews(animeId);

            //then
            assertDoesNotThrow(() -> shortReviewService.getShortReviews(animeId));
            assertEquals(response.getShortReview().get(0).getAnimeId(),animeId);
        }
    }


}