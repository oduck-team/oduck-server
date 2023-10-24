package io.oduck.api.domain.attractionPoint.service;

import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.IsAttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionElementRepository;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionPointServiceStub implements AttractionPointService {

    private final AttractionPointRepository attractionPointRepository;
    private final AttractionElementRepository attractionElementRepository;
    @Override
    public IsAttractionPoint isAttractionPoint(Long memberId, Long animeId) {
//        boolean drawing = false;
//        boolean story = false;
//        boolean music = false;
//        boolean character = false;
//        boolean voiceActor = false;
//
//        List<AttractionPoint> points = attractionElementRepository.findAllByAnimeId_memberId(memberId, animeId);
//        for (AttractionPoint point : points) {
//            switch (point.getAttractionElement()) {
//                case DRAWING -> drawing = true;
//                case MUSIC -> music = true;
//                case STORY -> story = true;
//                case CHARACTER -> character = true;
//                case VOICE_ACTOR -> voiceActor = true;
//            }
//        }
        return createAttractionPoint();
    }

    private IsAttractionPoint createAttractionPoint(){
        return IsAttractionPoint
                   .builder()
                   .drawing(true)
                   .story(true)
                   .music(false)
                   .character(false)
                   .voiceActor(false)
                   .build();
    }
}
