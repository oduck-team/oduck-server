package io.oduck.api.unit.starRating.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.oduck.api.domain.starRating.entity.StarRating;
import io.oduck.api.domain.starRating.repository.StarRatingRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class StarRatingRepositoryTest {
    @Autowired
    StarRatingRepository starRatingRepository;

    @Nested
    @DisplayName("회원 및 애니메 식별자로 별점 조회")
    class FindByMemberIdAndAnimeId {
        @DisplayName("회원 및 애니메이션 식별자로 별점 조회 성공")
        @Test
        void findByMemberIdAndAnimeIdSuccess() {
            // given
            Long memberId = 1L;
            Long animeId = 1L;

            // when
            Optional<StarRating> result = starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId);

            // then
            assertNotNull(result.get());
        }

        @DisplayName("회원 및 애니메이션 식별자로 별점 조회 실패")
        @Test
        void findByMemberIdAndAnimeIdFailure() {
            // given
            Long memberId = 1L;
            Long animeId = 10L;

            // when
            Optional<StarRating> result = starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId);

            // then
            assertFalse(result.isPresent());
        }
    }
}
