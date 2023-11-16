package io.oduck.api.domain.attractionPoint.service;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointReqDto.*;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.*;
import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;
import java.util.List;
import java.util.Optional;

import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionPointServiceImpl implements AttractionPointService {

    private final AttractionPointRepository attractionPointRepository;
    private final MemberRepository memberRepository;
    private final AnimeRepository animeRepository;

    @Override
    public IsAttractionPoint isAttractionPoint(Long memberId, Long animeId) {
        boolean drawing = false;
        boolean story = false;
        boolean music = false;
        boolean character = false;
        boolean voiceActor = false;

        List<AttractionPoint> points = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, animeId);
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

    @Override
    @Transactional
    public void save(Long memberId, AttractionPointReq req) {
        if(checkAttractionPoint(memberId, req.getAnimeId()).getIsAttractionPoint()){
            throw new BadRequestException("AttractionPoint is already exists.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member"));

        Anime anime = animeRepository.findById(req.getAnimeId())
                .orElseThrow(() -> new NotFoundException("Anime"));


        for(int i = 0; i < req.getAttractionElements().size(); i++){
            AttractionPoint attractionPoint = AttractionPoint
                                                .builder()
                                                .member(member)
                                                .anime(anime)
                                                .attractionElement(req.getAttractionElements().get(i))
                                                .build();
            attractionPointRepository.save(attractionPoint);
        }
    }

    @Override
    public CheckAttractionPoint checkAttractionPoint(Long memberId, Long animeId) {
        List<AttractionPoint> findPoint = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, animeId);
        return CheckAttractionPoint
                .builder()
                .isAttractionPoint(!findPoint.isEmpty())
                .build();
    }
}
