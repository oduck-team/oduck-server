package io.oduck.api.domain.attractionPoint.service;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointReqDto.*;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.*;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.attractionPoint.repository.AttractionPointRepository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.ConflictException;
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
        List<AttractionPoint> findPoint = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, req.getAnimeId());

        List<AttractionElement> elementList = findPoint.stream()
                .map(AttractionPoint::getAttractionElement)
                .toList();

        //중복되지 않은 포인트
        List<AttractionElement> matchPoint = req.getAttractionElements().stream()
                .filter(attractionElements ->
                        elementList.stream()
                                .noneMatch(Predicate.isEqual(attractionElements)))
                .toList();

        if (matchPoint.isEmpty()) {
            throw new ConflictException("AttractionPoint");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member"));

        Anime anime = animeRepository.findById(req.getAnimeId())
                .orElseThrow(() -> new NotFoundException("Anime"));

        List<AttractionPoint> points = matchPoint
                .stream()
                .map(attractionElement -> AttractionPoint
                        .builder()
                        .member(member)
                        .anime(anime)
                        .attractionElement(attractionElement)
                        .build())
                .toList();
        attractionPointRepository.saveAll(points);
    }

    @Override
    public CheckAttractionPoint checkAttractionPoint(Long memberId, Long animeId) {
        List<AttractionPoint> findPoint = attractionPointRepository.findAllByAnimeIdAndMemberId(memberId, animeId);
        return CheckAttractionPoint
                .builder()
                .isAttractionPoint(!findPoint.isEmpty())
                .build();
    }

    @Override
    public boolean update(Long memberId, Long attractionPointId, UpdateAttractionPoint req) {
        AttractionPoint findAttractionPoint = getAttractionPoint(attractionPointId);

        if (findAttractionPoint.getAttractionElement().equals(req.getAttractionElement())) {
            return false;
        }
        Long findMemberId = findAttractionPoint.getMember().getId();
        //입덕 포인트 작성자 인지 확인
        Optional
                .ofNullable(findMemberId)
                .ifPresent(
                        id -> {
                            if (!findMemberId.equals(memberId)) {
                                throw new BadRequestException("Not the author of the attractionPoint.");
                            }
                            findAttractionPoint.updateElement(req.getAttractionElement());
                        }
                );
        attractionPointRepository.save(findAttractionPoint);
        return true;
    }

    @Override
    public AttractionPointStats getAttractionPointStats(Long animeId) {
        //입덕포인트 / 전체 입덕포인트 개수
        Long totalCount = attractionPointRepository.count();
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

    private AttractionPoint getAttractionPoint(Long attractionPointId) {
        return attractionPointRepository.findById(attractionPointId)
                .orElseThrow(
                        () -> new NotFoundException("AttractionPoint")
                );
    }

    private double calculateElementRatio(AttractionElement element, Long animeId, Long totalCount) {
        Long countElementByAnimeId = attractionPointRepository.countElementByAnimeId(element, animeId);
        if (countElementByAnimeId <= 0) {
            return 0;
        }
        return (double) countElementByAnimeId / totalCount;
    }

}
