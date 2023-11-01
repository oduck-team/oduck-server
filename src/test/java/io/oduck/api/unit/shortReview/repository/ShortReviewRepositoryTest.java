
package io.oduck.api.unit.shortReview.repository;

import static io.oduck.api.global.utils.AnimeTestUtils.getBroadcastType;
import static io.oduck.api.global.utils.AnimeTestUtils.getEpisodeCount;
import static io.oduck.api.global.utils.AnimeTestUtils.getQuarter;
import static io.oduck.api.global.utils.AnimeTestUtils.getRating;
import static io.oduck.api.global.utils.AnimeTestUtils.getStatus;
import static io.oduck.api.global.utils.AnimeTestUtils.getSummary;
import static io.oduck.api.global.utils.AnimeTestUtils.getThumbnail;
import static io.oduck.api.global.utils.AnimeTestUtils.getTitle;
import static io.oduck.api.global.utils.AnimeTestUtils.getYear;
import static io.oduck.api.global.utils.AnimeTestUtils.isReleased;
import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.entity.AnimeGenre;
import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import io.oduck.api.domain.anime.entity.AnimeStudio;
import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ShortReviewRepositoryTest {

    @Autowired
    private ShortReviewRepository shortReviewRepository;
    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Nested
    @DisplayName("리뷰 등록")
    class PostShortReview{

        @Test
        @DisplayName("리뷰 등록 성공")
        void saveShortReview(){
            //given
            //리뷰 생성
            ShortReview shortReview = ShortReview
                                          .builder()
                                          .content("애니리뷰내용")
                                          .hasSpoiler(false)
                                          .build();

            //회원 생성
            Member member = Member.builder()
                                .build();
            Member saveMember = memberRepository.save(member);
            Long memberId = saveMember.getId();

            // 애니 생성
            List<AnimeStudio> animeStudios = new ArrayList<>();
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            List<AnimeGenre> animeGenres = new ArrayList<>();
            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();

            Anime createAnime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), isReleased(),
                animeOriginalAuthors, animeStudios, animeVoiceActors, animeGenres, null
            );

            Anime anime = animeRepository.saveAndFlush(createAnime);
            Long animeId = anime.getId();

            shortReview.relateMember(member);
            shortReview.relateAnime(anime);

            //when
            ShortReview saveShortReview = shortReviewRepository.save(shortReview);

            //then
            assertNotNull(saveShortReview);
            assertThat(saveShortReview.getAnime().getId()).isEqualTo(animeId);
            assertThat(saveShortReview.getMember().getId()).isEqualTo(memberId);
            assertThat(saveShortReview.getContent()).isEqualTo(shortReview.getContent());
            assertThat(saveShortReview.isHasSpoiler()).isEqualTo(shortReview.isHasSpoiler());
        }
    }
    @Nested
    @DisplayName("리뷰 조회")
    class GetShortReviews{

        @Test
        @DisplayName("리뷰 조회 성공")
        void getShortReviews(){
            //given
            Long animeId = 1L;
            Pageable pageable = applyPageableForNonOffset(10, "createdAt", "desc");

            Slice<ShortReviewDsl> shortReviewDsl = shortReviewRepository.selectShortReviews(animeId, null, pageable);

            assertNotNull(shortReviewDsl);
            assertNotNull(shortReviewDsl.getContent().get(0).getAnimeId());
            assertNotNull(shortReviewDsl.getContent().get(0).getName());
            assertNotNull(shortReviewDsl.getContent().get(0).getThumbnail());
            assertNotNull(shortReviewDsl.getContent().get(0).getScore());
            assertNotNull(shortReviewDsl.getContent().get(0).getContent());
            assertNotNull(shortReviewDsl.getContent().get(0).getLikeCount());
            assertNotNull(shortReviewDsl.getContent().get(0).getCreatedAt());
        }
    }


    @Nested
    @DisplayName("리뷰 수정")
    class PatchShortReview {

        @Test
        @DisplayName("리뷰 내용 수정 성공")
        void changeShortReviewContent() {
            //given
            Long reviewId = 1L;
            Long memberId = 1L;
            String content = "애니리뷰내용수정";
            boolean hasSpoiler = true;

            //회원 생성
            Member member = Member
                                .builder()
                                .id(memberId)
                                .build();

            memberRepository.save(member);

            //리뷰 생성
            ShortReview shortReview = ShortReview
                                          .builder()
                                          .content("애니리뷰내용")
                                          .hasSpoiler(hasSpoiler)
                                          .build();

            //애니 생성
            List<AnimeStudio> animeStudios = new ArrayList<>();
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            List<AnimeGenre> animeGenres = new ArrayList<>();
            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();

            Anime createAnime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), isReleased(),
                animeOriginalAuthors, animeStudios, animeVoiceActors, animeGenres, null
            );

            Anime anime = animeRepository.saveAndFlush(createAnime);


            shortReview.relateMember(member);
            shortReview.relateAnime(anime);

            ShortReview saveShortReview = shortReviewRepository.save(shortReview);

            //when
            ShortReview findShortReview = shortReviewRepository.findById(reviewId).get();
            Long animeId = findShortReview.getAnime().getId();

            //리뷰 수정
            findShortReview.updateContent(content);
            findShortReview.updateSpoiler(hasSpoiler);

            ShortReview updateShortReview = shortReviewRepository.save(findShortReview);

            //then
            assertNotNull(findShortReview);
            assertEquals(updateShortReview.getAnime().getId(), animeId);
            assertEquals(updateShortReview.getMember().getId(), memberId);
            assertEquals(updateShortReview.getContent(), content);
            assertEquals(updateShortReview.isHasSpoiler(), hasSpoiler);
        }
    }
}