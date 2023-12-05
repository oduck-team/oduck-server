package io.oduck.api.unit.attractionPoint.repository;

import static io.oduck.api.global.utils.AnimeTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;
import io.oduck.api.domain.member.entity.Member;

import java.util.List;

import io.oduck.api.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private MemberRepository memberRepository;

    Anime anime;
    Member member;
    @BeforeEach
    void setUp() {
        anime = createAnime();
        member = Member.builder().id(1L).build();
        memberRepository.save(member);
        animeRepository.save(anime);
    }

    @Nested
    @DisplayName("애니 입덕 포인트 조회")
    class IsAttractionPoint {

        @Test
        @DisplayName("입덕포인트 조회 성공")
        void isAttractionPoint() {
            //given
            //회원 생성
            Long pointId1 = 1L;
            Long pointId2 = 2L;

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
            List<AttractionPoint> res = attractionPointRepository.findAllByAnimeIdAndMemberId(member.getId(), anime.getId());
            AttractionElement findDrawing = res.get(0).getAttractionElement();
            AttractionElement findStory = res.get(1).getAttractionElement();

            //then
            assertNotNull(res);
            assertThat(res.get(0).getMember().getId()).isEqualTo(member.getId());
            assertThat(res.get(0).getAnime().getId()).isEqualTo(anime.getId());
            assertThat(findDrawing).isEqualTo(saveDrawing.getAttractionElement());
            assertThat(findStory).isEqualTo(saveStory.getAttractionElement());
        }
    }
    @Nested
    @DisplayName("입덕 포인트 생성")
    class PostAttractionPoint{

        @Test
        @DisplayName("입덕포인트 추가 성공 시 status 200 반환")
        void saveAttractionPoint(){
            //given
            List<AttractionPoint> find = attractionPointRepository.findAllByAnimeIdAndMemberId(member.getId(), anime.getId());

            //입덕포인트 등록
            AttractionPoint drawing = AttractionPoint
                    .builder()
                    .member(member)
                    .anime(anime)
                    .attractionElement(AttractionElement.DRAWING)
                    .build();
            AttractionPoint story = AttractionPoint
                    .builder()
                    .member(member)
                    .anime(anime)
                    .attractionElement(AttractionElement.STORY)
                    .build();

            //when
            AttractionPoint saveDrawing = attractionPointRepository.save(drawing);
            AttractionPoint saveStory = attractionPointRepository.save(story);

            //then
            assertNotNull(anime);
            assertNotNull(member);
            assertTrue(find.isEmpty());
            assertThat(saveDrawing.getAttractionElement()).isEqualTo(drawing.getAttractionElement());
            assertThat(saveStory.getAttractionElement()).isEqualTo(story.getAttractionElement());
        }

    }

    @Nested
    @DisplayName("입덕 포인트 존재 유무")
    class GetAttractionPoint {

        @Test
        @DisplayName("입덕포인트 존재 시 true, 부재 시 false")
        void checkAttractionPoint() {
            //given

            List<AttractionPoint> find = attractionPointRepository.findAllByAnimeIdAndMemberId(member.getId(), anime.getId());

            //입덕포인트 등록
            AttractionPoint drawing = AttractionPoint
                    .builder()
                    .member(member)
                    .anime(anime)
                    .attractionElement(AttractionElement.DRAWING)
                    .build();
            AttractionPoint story = AttractionPoint
                    .builder()
                    .member(member)
                    .anime(anime)
                    .attractionElement(AttractionElement.STORY)
                    .build();

            //when
            AttractionPoint saveDrawing = attractionPointRepository.save(drawing);
            AttractionPoint saveStory = attractionPointRepository.save(story);

            //then
            assertNotNull(anime);
            assertNotNull(member);
            assertTrue(find.isEmpty());
            assertThat(saveDrawing.getAttractionElement()).isEqualTo(drawing.getAttractionElement());
            assertThat(saveStory.getAttractionElement()).isEqualTo(story.getAttractionElement());
        }
    }

    @Nested
    @DisplayName("입덕 포인트 수정")
    class PatchAttractionPoint{

        @Test
        @DisplayName("입덕 포인트 수정 성공")
        void patchAttractionPointSuccess(){
            //given
            //입덕포인트 등록
            AttractionPoint drawing = AttractionPoint
                    .builder()
                    .member(member)
                    .anime(anime)
                    .attractionElement(AttractionElement.DRAWING)
                    .build();

            AttractionPoint point = attractionPointRepository.save(drawing);

            AttractionPoint find = attractionPointRepository.findById(point.getId()).get();
            find.updateElement(AttractionElement.CHARACTER);

            //when
            AttractionPoint update = attractionPointRepository.save(find);
            //then
            assertNotNull(find);
            assertEquals(find.getAnime().getId(), anime.getId());
            assertEquals(find.getMember().getId(), member.getId());
            assertEquals(update.getAttractionElement(), AttractionElement.CHARACTER);
        }
    }
}

