package io.oduck.api.unit.attractionPoint.service;

import static io.oduck.api.global.utils.AnimeTestUtils.createAnime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointReqDto.*;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.IsAttractionPoint;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;
import io.oduck.api.domain.attractionPoint.service.AttractionPointServiceImpl;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AttractionPointServiceTest {
    @InjectMocks
    private AttractionPointServiceImpl attractionPointService;
    @Mock
    private AttractionPointRepository attractionPointRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AnimeRepository animeRepository;

    Member member = Member
            .builder()
            .id(1L)
            .build();
    Anime anime = createAnime();

    @Nested
    @DisplayName("입덕 포인트 추가")
    class PostAttractionPoint{

        @Test
        @DisplayName("입덕 포인트 추가 성공 시 status 200 반환")
        void saveAttractionPoint(){
            //given
            List<AttractionPoint> list = new ArrayList<>();
            List<AttractionElement> elementList = new ArrayList<>();
            elementList.add(AttractionElement.DRAWING);
            elementList.add(AttractionElement.STORY);

            given(attractionPointRepository.findAllByAnimeIdAndMemberId(anyLong(), anyLong()))
                    .willReturn(list);
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(animeRepository.findById(anyLong())).willReturn(Optional.ofNullable(anime));

            AttractionPointReq req = AttractionPointReq
                    .builder()
                    .animeId(1L)
                    .attractionElements(elementList)
                    .build();

            //when
            attractionPointService.save(member.getId(),req);

            //then
            assertDoesNotThrow(() -> attractionPointService.save(member.getId(), req));
        }
    }

    @Nested
    @DisplayName("입덕 포인트 조회")
    class GetAttractionPoint{

        @Test
        @DisplayName("입덕 포인트 존재 시 true, false 판별")
        void isAttractionPoint(){
            //given
            Long memberId = 1L;
            Long animeId = 1L;

            //when
            IsAttractionPoint res = attractionPointService.isAttractionPoint(memberId, animeId);

            //then
            assertDoesNotThrow(() -> attractionPointService.isAttractionPoint(memberId, animeId));
        }
    }

    @Nested
    @DisplayName("입덕 포인트 수정")
    class PatchAttractionPoint{

        @Test
        @DisplayName("입덕 포인트 수정 성공")
        void patchAttractionPointSuccess(){
            //given
            List<AttractionPoint> list = new ArrayList<>();
            AttractionPoint point = AttractionPoint
                    .builder()
                    .member(member)
                    .anime(anime)
                    .attractionElement(AttractionElement.DRAWING)
                    .build();
            list.add(point);
            List<AttractionElement> elementList = new ArrayList<>();
            elementList.add(AttractionElement.VOICE_ACTOR);
            elementList.add(AttractionElement.CHARACTER);

            given(attractionPointRepository.findAllByAnimeIdAndMemberId(anyLong(), anyLong()))
                    .willReturn(list);
            AttractionPointReq update = AttractionPointReq
                    .builder()
                    .animeId(1L)
                    .attractionElements(elementList)
                    .build();

            //when
            boolean updateAttractionPoint = attractionPointService.update(member.getId(), update);

            //then
            assertTrue(updateAttractionPoint);
        }

        @Test
        @DisplayName("입덕 포인트 수정 실패")
        void patchAttractionPointFalse(){
            //given
            List<AttractionPoint> list = new ArrayList<>();
            List<AttractionElement> elementList = new ArrayList<>();
            elementList.add(AttractionElement.DRAWING);
            elementList.add(AttractionElement.STORY);

            given(attractionPointRepository.findAllByAnimeIdAndMemberId(anyLong(), anyLong()))
                    .willReturn(list);

            AttractionPointReq update = AttractionPointReq
                    .builder()
                    .animeId(1L)
                    .attractionElements(elementList)
                    .build();

            //when
            boolean updateAttractionPoint = attractionPointService.update(member.getId(),update);

            //then
            assertFalse(updateAttractionPoint);
        }
    }
}
