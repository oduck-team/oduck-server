package io.oduck.api.unit.starRating.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.starRating.dto.StarRatingResDto.RatedDateTimeRes;
import io.oduck.api.domain.starRating.entity.StarRating;
import io.oduck.api.domain.starRating.repository.StarRatingRepository;
import io.oduck.api.domain.starRating.service.StarRatingServiceImpl;
import io.oduck.api.global.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StarRatingServiceTest {
    @InjectMocks
    StarRatingServiceImpl starRatingService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AnimeRepository animeRepository;

    @Mock
    StarRatingRepository starRatingRepository;

    @Nested
    @DisplayName("별점 생성")
    class CreateScore {
        Long memberId = 1L;
        Long animeId = 1L;
        int score = 5;

        Member member = Member.builder()
            .id(memberId)
            .build();
        Anime anime = Anime.builder()
            .id(animeId)
            .build();

        @Test
        @DisplayName("별점 생성 성공")
        void createScore() {
            // given
            given(starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId))
                .willReturn(Optional.empty());
            given(memberRepository.findById(memberId))
                .willReturn(Optional.ofNullable(member));
            given(animeRepository.findById(animeId))
                .willReturn(Optional.ofNullable(anime));

            // when
            boolean result = starRatingService.createScore(memberId, animeId, score);

            // then
            assertDoesNotThrow(() -> starRatingService.createScore(memberId, animeId, score));
            assertTrue(result);
        }

        @Test
        @DisplayName("별점 생성 실패")
        void createScoreIfAlreadyExist() {
            // given

            StarRating starRating = StarRating.builder()
                .member(member)
                .anime(anime)
                .score(score)
                .build();

            given(starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId))
                .willReturn(Optional.ofNullable(starRating));

            // when
            boolean result = starRatingService.createScore(memberId, animeId, score);

            // then
            assertDoesNotThrow(() -> starRatingService.createScore(memberId, animeId, score));
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("별점 조회")
    class GetScore {
        Long memberId = 1L;
        Long animeId = 1L;
        int score = 5;

        Member member = Member.builder()
            .id(memberId)
            .build();
        Anime anime = Anime.builder()
            .id(animeId)
            .build();

        @Test
        @DisplayName("별점 조회 성공")
        void chekRated() {
            // given
            StarRating starRating = StarRating.builder()
                .member(member)
                .anime(anime)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();

            given(starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId))
                .willReturn(Optional.ofNullable(starRating));

            // when
            StarRating foundStarRating = starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId)
                .orElseThrow(() -> new RuntimeException("StarRating"));

            RatedDateTimeRes createdAtScore = starRatingService.checkRated(memberId, animeId);

            // then
            assertDoesNotThrow(() -> starRatingService.checkRated(memberId, animeId));
            assertNotNull(createdAtScore);
        }

        @DisplayName("별점 조회 실패")
        @Test
        void chekRatedIfNotExist() {
            // given
            given(starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId))
                .willReturn(Optional.empty());

            // when
            // then
            assertThrows(NotFoundException.class, () -> starRatingService.checkRated(memberId, animeId));
        }
    }
}
