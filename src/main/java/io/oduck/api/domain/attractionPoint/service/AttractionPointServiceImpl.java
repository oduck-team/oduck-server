package io.oduck.api.domain.attractionPoint.service;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointReqDto.*;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.*;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.ConflictException;
import io.oduck.api.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionPointServiceImpl implements AttractionPointService {

    private final AttractionPointRepository attractionPointRepository;
    private final MemberRepository memberRepository;
    private final AnimeRepository animeRepository;

    @Override
    public IsAttractionPoint isAttractionPoint(Long memberId, Long animeId) {
        List<AttractionPoint> points = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, animeId);

        boolean drawing = false, story = false, music = false, character = false, voiceActor = false;

        for (AttractionPoint point : points) {
            switch (point.getAttractionElement()) {
                case DRAWING -> drawing = true;
                case MUSIC -> music = true;
                case STORY -> story = true;
                case CHARACTER -> character = true;
                default -> voiceActor = true;
            }
        }

        return IsAttractionPoint.builder()
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
        List<AttractionPoint> findPoint = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, req.getAnimeId());

        List<AttractionElement> matchPoint = findNonOverlappingValues(findPoint, req);
        if (matchPoint.isEmpty()) {
            throw new ConflictException("AttractionPoint");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member"));

        Anime anime = animeRepository.findById(req.getAnimeId())
                .orElseThrow(() -> new NotFoundException("Anime"));

        attractionPointRepository.saveAll(getAttractionPoint(matchPoint, member, anime));
    }

    @Override
    public CheckAttractionPoint checkAttractionPoint(Long memberId, Long animeId) {
        List<AttractionPoint> findPoint = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, animeId);
        return CheckAttractionPoint.builder()
                .isAttractionPoint(!findPoint.isEmpty())
                .build();
    }

    @Override
    public boolean update(Long memberId, AttractionPointReq req) {
        List<AttractionPoint> findPoint = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, req.getAnimeId());

        if (findPoint.isEmpty()) {
            return false;
        }

        Long findMemberId = findPoint.get(0).getMember().getId();

        if (!findMemberId.equals(memberId)) {
            throw new BadRequestException("Not the author of the attractionPoint.");
        }

        List<AttractionElement> findAttractionElement = findNonOverlappingValues(findPoint, req);

        if (findAttractionElement.isEmpty()) {
            throw new ConflictException("AttractionPoint");
        } else {
            attractionPointRepository.deleteAllInBatch(findPoint);

            Member member = findPoint.get(0).getMember();
            Anime anime = findPoint.get(0).getAnime();

            attractionPointRepository.saveAll(getAttractionPoint(findAttractionElement, member, anime));
        }
        return true;
    }

    @Override
    public AttractionPointStats getAttractionPointStats(Long animeId) {
        Long totalCount = attractionPointRepository.countByAnimeId(animeId);
        double drawing = calculateElementRatio(AttractionElement.DRAWING, animeId, totalCount);
        double story = calculateElementRatio(AttractionElement.STORY, animeId, totalCount);
        double voiceActor = calculateElementRatio(AttractionElement.VOICE_ACTOR, animeId, totalCount);
        double music = calculateElementRatio(AttractionElement.MUSIC, animeId, totalCount);
        double character = calculateElementRatio(AttractionElement.CHARACTER, animeId, totalCount);

        return AttractionPointStats.builder()
                .drawing(drawing)
                .voiceActor(voiceActor)
                .story(story)
                .music(music)
                .character(character)
                .build();
    }

    private List<AttractionPoint> getAttractionPoint(List<AttractionElement> elements, Member member, Anime anime){
        return elements.stream()
                .map(attractionElement -> AttractionPoint.builder()
                        .member(member)
                        .anime(anime)
                        .attractionElement(attractionElement)
                        .build())
                .collect(Collectors.toList());
    }
    private double calculateElementRatio(AttractionElement element, Long animeId, Long totalCount) {
        Long countElementByAnimeId = attractionPointRepository.countElementByAnimeId(element, animeId);
        return countElementByAnimeId > 0 ? (double) countElementByAnimeId / totalCount * 100 : 0;
    }

    private List<AttractionElement> findNonOverlappingValues(List<AttractionPoint> findPoint, AttractionPointReq req) {
        List<AttractionElement> elementList = findPoint.stream()
                .map(AttractionPoint::getAttractionElement)
                .toList();
        //중복되지 않은 값
        return req.getAttractionElements().stream()
                .filter(attractionElement -> !elementList.contains(attractionElement))
                .collect(Collectors.toList());
    }
}