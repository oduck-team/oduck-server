package io.oduck.api.unit.attractionPoint.repository;

import static io.oduck.api.global.utils.AnimeTestUtils.getBroadcastType;
import static io.oduck.api.global.utils.AnimeTestUtils.getEpisodeCount;
import static io.oduck.api.global.utils.AnimeTestUtils.getQuarter;
import static io.oduck.api.global.utils.AnimeTestUtils.getRating;
import static io.oduck.api.global.utils.AnimeTestUtils.getStatus;
import static io.oduck.api.global.utils.AnimeTestUtils.getSummary;
import static io.oduck.api.global.utils.AnimeTestUtils.getThumbnail;
import static io.oduck.api.global.utils.AnimeTestUtils.getTitle;
import static io.oduck.api.global.utils.AnimeTestUtils.getYear;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.entity.AnimeGenre;
import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import io.oduck.api.domain.anime.entity.AnimeStudio;
import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;
import io.oduck.api.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
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
public class AttractionPointRepositoryTest {

    @Autowired
    private AttractionPointRepository attractionPointRepository;

    @Autowired
    private AnimeRepository animeRepository;


    @Nested
    @DisplayName("애니 입덕 포인트 조회")
    class isAttractionPoint{

        @Test
        @DisplayName("입덕포인트 조회 성공")
        void isAttactionPoint(){
            //given
            //회원 생성
            Long memberId = 1L;
            Long pointId1 = 1L;
            Long pointId2 = 2L;
            Member member = Member.builder()
                                .id(memberId)
                                .build();

            // 애니 생성
            List<AnimeStudio> animeStudios = new ArrayList<>();
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            List<AnimeGenre> animeGenres = new ArrayList<>();
            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();

            Anime createAnime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), animeOriginalAuthors,
                animeStudios, animeVoiceActors, animeGenres, null
            );

            Anime anime = animeRepository.saveAndFlush(createAnime);
            Long animeId = anime.getId();

            //입덕포인트 등록
            AttractionPoint drawing = AttractionPoint
                                        .builder()
                                        .id(pointId1)
                                        .member(member)
                                        .anime(anime)
                                        .attractionElement(AttractionElement.DRAWING)
                                        .build();
            AttractionPoint story = AttractionPoint
                                        .builder()
                                        .id(pointId2)
                                        .member(member)
                                        .anime(anime)
                                        .attractionElement(AttractionElement.STORY)
                                        .build();

            AttractionPoint saveDrawing = attractionPointRepository.save(drawing);
            AttractionPoint saveStory = attractionPointRepository.save(story);

            //when
            //입덕포인트 조회
            List<AttractionPoint> res = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, animeId);
            AttractionElement findDrawing = res.get(0).getAttractionElement();
            AttractionElement findStory = res.get(1).getAttractionElement();

            //then
            assertNotNull(res);
            assertThat(res.get(0).getMember().getId()).isEqualTo(memberId);
            assertThat(res.get(0).getAnime().getId()).isEqualTo(animeId);
            assertThat(findDrawing).isEqualTo(saveDrawing.getAttractionElement());
            assertThat(findStory).isEqualTo(saveStory.getAttractionElement());
        }
    }



}

