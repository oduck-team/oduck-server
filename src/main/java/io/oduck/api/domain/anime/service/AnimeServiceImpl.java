package io.oduck.api.domain.anime.service;

import io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchGenreIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchOriginalAuthorIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchSeriesIdReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchStudioIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchVoiceActorIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PostReq;
import io.oduck.api.domain.anime.dto.AnimeRes;
import io.oduck.api.domain.anime.dto.VoiceActorReq;
import io.oduck.api.domain.anime.dto.VoiceActorRes;
import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.entity.AnimeGenre;
import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import io.oduck.api.domain.anime.entity.AnimeStudio;
import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.genre.entity.Genre;
import io.oduck.api.domain.genre.repository.GenreRepository;
import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import io.oduck.api.domain.originalAuthor.repository.OriginalAuthorRepository;
import io.oduck.api.domain.series.entity.Series;
import io.oduck.api.domain.series.repository.SeriesRepository;
import io.oduck.api.domain.studio.entity.Studio;
import io.oduck.api.domain.studio.repository.StudioRepository;
import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import io.oduck.api.domain.voiceActor.repository.VoiceActorRepository;
import io.oduck.api.global.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnimeServiceImpl implements AnimeService{

    private final AnimeRepository animeRepository;
    private final OriginalAuthorRepository originalAuthorRepository;
    private final VoiceActorRepository voiceActorRepository;
    private final StudioRepository studioRepository;
    private final GenreRepository genreRepository;
    private final SeriesRepository seriesRepository;

    @Override
    public AnimeRes getAnimeById(Long animeId) {
        //TODO: 애니 상세 조회 구현
        AnimeRes anime = createAnimeDto(animeId);

        return anime;
    }

    @Override
    public void save(PostReq postReq) {
        // 원작 작가
        List<Long> originalAuthorIds = postReq.getOriginalAuthorIds();
        List<OriginalAuthor> originalAuthors = originalAuthorRepository.findAllById(originalAuthorIds);

        List<AnimeOriginalAuthor> animeOriginalAuthors = originalAuthors.stream()
            .map(AnimeOriginalAuthor::createAnimeOriginalAuthor)
            .collect(Collectors.toList());

        // 애니에 참여한 성우 리스트
        List<VoiceActorReq> voiceActorDtoList = postReq.getVoiceActors();

        // 성우의 아이디 리스트 구하기
        List<Long> voiceActorIds = voiceActorDtoList.stream()
            .map(VoiceActorReq::getId)
            .collect(Collectors.toList());

        List<VoiceActor> voiceActors = voiceActorRepository.findAllById(voiceActorIds);

        // Id와 Part로 구성된 map 생성
        Map<Long, String> voiceActorDtoMap = voiceActorDtoList.stream()
            .collect(Collectors.toMap(VoiceActorReq::getId, VoiceActorReq::getPart));

        List<AnimeVoiceActor> animeVoiceActors = voiceActors.stream()
            .filter(voiceActor -> voiceActorDtoMap.containsKey(voiceActor.getId()))
            .map(voiceActor -> AnimeVoiceActor.createAnimeVoiceActor(voiceActorDtoMap.get(voiceActor.getId()), voiceActor))
            .collect(Collectors.toList());

        // 스튜디오
        List<Long> studioIds = postReq.getStudioIds();
        List<Studio> studios = studioRepository.findAllById(studioIds);

        List<AnimeStudio> animeStudios = studios.stream()
            .map(AnimeStudio::createAnimeStudio)
            .collect(Collectors.toList());

        // 장르
        List<Long> genreIds = postReq.getGenreIds();

        List<Genre> genres = genreRepository.findAllById(genreIds);

        List<AnimeGenre> animeGenres = genres.stream()
            .map(AnimeGenre::createAnimeGenre)
            .collect(Collectors.toList());

        // 시리즈
        Long seriesId = postReq.getSeriesId();
        Series series = createSeries(seriesId);


        Anime anime = Anime.createAnime(postReq.getTitle(), postReq.getSummary(), postReq.getBroadcastType(), postReq.getEpisodeCount(), postReq.getThumbnail(),
            postReq.getYear(), postReq.getQuarter(), postReq.getRating(), postReq.getStatus(), animeOriginalAuthors, animeStudios, animeVoiceActors, animeGenres, series);

        animeRepository.save(anime);
    }


    private Series createSeries(Long seriesId) {
        if(seriesId == null){
            return null;
        }else{
            return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NotFoundException("Series"));
        }
    }

    @Override
    public void update(Long animeId, PatchAnimeReq req) {
        Anime anime = findAnime(animeId);

        anime.update(req.getTitle(), req.getSummary(), req.getBroadcastType(), req.getEpisodeCount(), req.getThumbnail(), req.getYear(),
            req.getQuarter(), req.getRating(), req.getStatus());
    }

    @Override
    public void updateAnimeOriginalAuthors(Long animeId,
        PatchOriginalAuthorIdsReq patchReq) {

        Anime anime = findAnime(animeId);

        List<Long> originalAuthorIds = patchReq.getOriginalAuthorIds();
        List<OriginalAuthor> originalAuthors = originalAuthorRepository.findAllById(originalAuthorIds);

        List<AnimeOriginalAuthor> animeOriginalAuthors = originalAuthors.stream()
            .map(AnimeOriginalAuthor::createAnimeOriginalAuthor)
            .collect(Collectors.toList());

        anime.updateAnimeOriginalAuthors(animeOriginalAuthors);
    }

    @Override
    public void updateAnimeStudios(Long animeId, PatchStudioIdsReq patchReq) {
        Anime anime = findAnime(animeId);
        List<Long> studioIds = patchReq.getStudioIds();

        List<Studio> studios = studioRepository.findAllById(studioIds);
        List<AnimeStudio> animeStudios = studios.stream()
            .map(AnimeStudio::createAnimeStudio)
            .collect(Collectors.toList());

        anime.updateAnimeStudios(animeStudios);
    }

    @Override
    public void updateAnimeVoiceActors(Long animeId, PatchVoiceActorIdsReq patchReq) {
        Anime anime = findAnime(animeId);

        // 애니에 참여한 성우 리스트
        List<VoiceActorReq> voiceActorDtoList = patchReq.getVoiceActors();

        // 성우의 아이디 리스트 구하기
        List<Long> voiceActorIds = voiceActorDtoList.stream()
            .map(VoiceActorReq::getId)
            .collect(Collectors.toList());

        List<VoiceActor> voiceActors = voiceActorRepository.findAllById(voiceActorIds);

        // Id와 Part로 구성된 map 생성
        Map<Long, String> voiceActorDtoMap = voiceActorDtoList.stream()
            .collect(Collectors.toMap(VoiceActorReq::getId, VoiceActorReq::getPart));

        List<AnimeVoiceActor> animeVoiceActors = voiceActors.stream()
            .filter(va -> voiceActorDtoMap.containsKey(va.getId()))
            .map(voiceActor -> AnimeVoiceActor.createAnimeVoiceActor(voiceActorDtoMap.get(voiceActor.getId()), voiceActor))
            .collect(Collectors.toList());

        anime.updateAnimeVoiceActors(animeVoiceActors);
    }

    @Override
    public void updateAnimeGenres(Long animeId, PatchGenreIdsReq patchReq) {
        Anime anime = findAnime(animeId);

        List<Long> genreIds = patchReq.getGenreIds();
        List<Genre> genres = genreRepository.findAllById(genreIds);

        List<AnimeGenre> animeGenres = genres.stream()
            .map(AnimeGenre::createAnimeGenre)
            .collect(Collectors.toList());

        anime.updateAnimeGenre(animeGenres);
    }

    @Override
    public void updateSeries(Long animeId, PatchSeriesIdReq patchReq) {
        Anime anime = findAnime(animeId);

        Long seriesId = patchReq.getSeriesId();
        Series series = seriesRepository.findById(seriesId)
            .orElseThrow(() -> new NotFoundException("Series"));

        anime.update(series);
    }

    @Transactional(readOnly = true)
    public Anime findAnime(Long animeId) {
        return animeRepository.findById(animeId).orElseThrow(() -> new NotFoundException("Anime"));
    }

    private AnimeRes createAnimeDto(Long animeId) {
        return AnimeRes.builder()
            .id(animeId)
            .title("귀멸의 칼날: 도공 마을편")
            .thumbnail("https://image파일경로/uuid.jpg")
            .broadcastType(BroadcastType.TVA)
            .year(2023)
            .quarter(Quarter.Q2)
            .summary(
                "113년 만에 상현 혈귀가 죽자 분개한 무잔은 나머지 상현 혈귀들에게 또 다른 명령을 내린다! 한편, 규타로와의 전투 도중 검이 심하게 손상된 탄지로에게 하가네즈카는 대 격노하고 탄지로는 그 검을 만든 대장장이 하가네즈카 호타루에게 검이 어떻게 심하게 손상되었는지 설명하기 위해 도공 마을을 방문한다. 탄지로가 검이 수리되기를 기다리는 동안, 상현 혈귀 한텐구와 쿗코가 숨겨진 마을인 ‘도공 마을'을 습격한다. 공격할 때마다 분열해서 위력이 커지는 한텐구로 인해 탄지로와 겐야는 고전을 면치 못한다. 한편, 타인에 대한 관심이 희박한 하주 토키토 무이치로는 혈귀들에게 공격당하고 있는 코테츠를 목격하는데….")
            .episodeCount(11)
            .rating(Rating.ADULT)
            .status(Status.FINISHED)
            .genres(getGenres())
            .originalAuthors(getOriginalAuthors())
            .voiceActors(getVoiceActors())
            .studios(getStudios())
            .reviewCount(172)
            .bookmarkCount(72)
            .build();
    }

    private List<String> getStudios() {
        List<String> studios = new ArrayList<>();
        studios.add("ufotable");
        return studios;
    }

    private List<VoiceActorRes> getVoiceActors() {
        List<VoiceActorRes> voiceActors = new ArrayList<>();

        for(int i = 0; i<5; i++){
            VoiceActorRes voiceActor = new VoiceActorRes("성우"+i, "카마도 탄지로"+i);
            voiceActors.add(voiceActor);
        }

        return voiceActors;
    }

    private List<String> getGenres(){
        List<String> genres = new ArrayList<>();
        genres.add("판타지");
        genres.add("액션");
        return genres;
    }

    private List<String> getOriginalAuthors() {
        List<String> originalAuthors = new ArrayList<>();
        originalAuthors.add("고토게 코요하루");
        return originalAuthors;
    }
}
