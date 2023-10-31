package io.oduck.api.domain.starRating.service;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.starRating.entity.StarRating;
import io.oduck.api.domain.starRating.repository.StarRatingRepository;
import io.oduck.api.global.exception.NotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StarRatingServiceImpl implements StarRatingService {
    private final MemberRepository memberRepository;
    private final AnimeRepository animeRepository;
    private final StarRatingRepository starRatingRepository;

    @Override
    public boolean createScore(Long memberId, Long animeId, int score) {
        Optional<StarRating> foundStarRating = starRatingRepository.findByMemberIdAndAnimeId(memberId, animeId);
        if (foundStarRating.isPresent()) {
            return false;
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("Member"));

        Anime anime = animeRepository.findById(animeId)
            .orElseThrow(() -> new NotFoundException("Anime"));

        StarRating starRating = StarRating.builder()
            .member(member)
            .anime(anime)
            .score(score)
            .build();

        starRatingRepository.save(starRating);
        return true;
    }
}
