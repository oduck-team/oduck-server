package io.oduck.api.domain.attractionPoint.service;

import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.IsAttractionPoint;
import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionPointServiceImpl implements AttractionPointService {

    private final AttractionPointRepository attractionPointRepository;
    @Override
    public IsAttractionPoint isAttractionPoint(Long memberId, Long animeId) {
        boolean drawing = false;
        boolean story = false;
        boolean music = false;
        boolean character = false;
        boolean voiceActor = false;

        List<AttractionPoint> points = attractionPointRepository.findAllByAnimeId_memberId(memberId, animeId);
        for (AttractionPoint point : points) {
            switch (point.getAttractionElement()) {
                case DRAWING -> drawing = true;
                case MUSIC -> music = true;
                case STORY -> story = true;
                case CHARACTER -> character = true;
                default -> voiceActor = true;
            }
        }
        return IsAttractionPoint
                      .builder()
                      .drawing(drawing)
                      .story(story)
                      .music(music)
                      .character(character)
                      .voiceActor(voiceActor)
                      .build();
    }
}
