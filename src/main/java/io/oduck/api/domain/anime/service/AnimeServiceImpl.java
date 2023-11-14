package io.oduck.api.domain.anime.service;

import static io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchGenreIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchOriginalAuthorIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchSeriesIdReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchStudioIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchVoiceActorIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PostReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.Sort;
import static io.oduck.api.domain.anime.dto.AnimeRes.DetailResult;
import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;

import io.oduck.api.domain.admin.dto.AdminReq;
import io.oduck.api.domain.admin.dto.AdminReq.QueryType;
import io.oduck.api.domain.admin.dto.AdminRes;
import io.oduck.api.domain.anime.dto.AnimeRes.StarRatingAvg;
import io.oduck.api.domain.anime.dto.AnimeVoiceActorReq;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.entity.AnimeGenre;
import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import io.oduck.api.domain.anime.entity.AnimeStudio;
import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import io.oduck.api.domain.anime.repository.AnimeGenreRepository;
import io.oduck.api.domain.anime.repository.AnimeOriginalAuthorRepository;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.anime.repository.AnimeStudioRepository;
import io.oduck.api.domain.anime.repository.AnimeVoiceActorRepository;
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
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.PageResponse;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.exception.NotFoundException;
import io.oduck.api.global.utils.PagingUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnimeServiceImpl implements AnimeService{

    private final AnimeRepository animeRepository;

    private final OriginalAuthorRepository originalAuthorRepository;
    private final AnimeOriginalAuthorRepository animeOriginalAuthorRepository;

    private final VoiceActorRepository voiceActorRepository;
    private final AnimeVoiceActorRepository animeVoiceActorRepository;

    private final StudioRepository studioRepository;
    private final AnimeStudioRepository animeStudioRepository;

    private final GenreRepository genreRepository;
    private final AnimeGenreRepository animeGenreRepository;

    private final SeriesRepository seriesRepository;

    @Override
    public void save(PostReq postReq) {
        // 원작 작가
        List<Long> originalAuthorIds = postReq.getOriginalAuthorIds();
        List<OriginalAuthor> originalAuthors = originalAuthorRepository.findAllById(originalAuthorIds);

        List<AnimeOriginalAuthor> animeOriginalAuthors = originalAuthors.stream()
            .map(AnimeOriginalAuthor::createAnimeOriginalAuthor)
            .collect(Collectors.toList());

        // 애니에 참여한 성우 리스트
        List<AnimeVoiceActorReq> voiceActorDtoList = postReq.getVoiceActors();

        // 성우의 아이디 리스트 구하기
        List<Long> voiceActorIds = voiceActorDtoList.stream()
            .map(AnimeVoiceActorReq::getId)
            .collect(Collectors.toList());

        List<VoiceActor> voiceActors = voiceActorRepository.findAllById(voiceActorIds);

        // Id와 Part로 구성된 map 생성
        Map<Long, String> voiceActorDtoMap = voiceActorDtoList.stream()
            .collect(Collectors.toMap(AnimeVoiceActorReq::getId, AnimeVoiceActorReq::getPart));

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
        boolean isSeriesIdNull = isIdNull(seriesId);
        Series series = findSeriesWhenIdNotNull(seriesId, isSeriesIdNull);

        Anime anime = Anime.createAnime(
            postReq.getTitle(), postReq.getSummary(), postReq.getBroadcastType(), postReq.getEpisodeCount(),
            postReq.getThumbnail(), postReq.getYear(), postReq.getQuarter(), postReq.getRating(), postReq.getStatus(),
            postReq.isReleased(), animeOriginalAuthors, animeStudios, animeVoiceActors, animeGenres, series
        );

        animeRepository.save(anime);
    }

    @Override
    @Transactional(readOnly = true)
    public DetailResult getAnimeById(Long animeId) {

        Anime anime = animeRepository.findAnimeByConditions(animeId, true)
                .orElseThrow(() -> new NotFoundException("Anime"));

        anime.increaseViewCount();

        List<AnimeOriginalAuthor> animeOriginalAuthors = animeOriginalAuthorRepository.findAllFetchByAnimeId(animeId);
        List<AnimeVoiceActor> animeVoiceActors = animeVoiceActorRepository.findAllFetchByAnimeId(animeId);
        List<AnimeStudio> animeStudios = animeStudioRepository.findAllFetchByAnimeId(animeId);
        List<AnimeGenre> animeGenres = animeGenreRepository.findAllFetchByAnimeId(animeId);

        return new DetailResult(anime, animeOriginalAuthors, animeVoiceActors, animeStudios, animeGenres);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<SearchResult> getSliceByCondition(String query, String cursor, Sort sort, OrderDirection order, int size, SearchFilterDsl searchFilterDsl) {
        Slice<SearchResult> slice = animeRepository.findSliceByCondition(
            query,
            cursor,
            PagingUtils.applyPageableForNonOffset(
                size,
                sort.getSort(),
                order.getOrder()
            ),
            searchFilterDsl
        );

        List<SearchResult> items = slice.getContent();

        return SliceResponse.of(slice, items, sort.getSort());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AdminRes.SearchResult> getPageByCondition(String query, QueryType queryType,
        int page, int size, AdminReq.Sort sort, OrderDirection order, AdminReq.SearchFilter searchFilter) {
        return animeRepository.findPageByCondition(
            query,
            queryType,
            PagingUtils.applyPageableForOffset(
                page,
                sort.getSort(),
                order.getOrder(),
                size
            ),
            searchFilter
        );
    }

    private Series findSeriesWhenIdNotNull(Long seriesId, boolean isSeriesIdNull) {
        if(isSeriesIdNull == true){
            return null;
        }

        return seriesRepository.findById(seriesId)
            .orElseThrow(() -> new NotFoundException("Series"));
    }

    private boolean isIdNull(Long id) {
        return id == null;
    }

    @Override
    public void update(Long animeId, PatchAnimeReq patchReq) {
        Anime anime = findAnime(animeId);

        anime.update(
            patchReq.getTitle(), patchReq.getSummary(), patchReq.getBroadcastType(), patchReq.getEpisodeCount(), patchReq.getThumbnail(), patchReq.getYear(),
            patchReq.getQuarter(), patchReq.getRating(), patchReq.getStatus(), patchReq.isReleased()
        );
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
        List<AnimeVoiceActorReq> voiceActorDtoList = patchReq.getVoiceActors();

        // 성우의 아이디 리스트 구하기
        List<Long> voiceActorIds = voiceActorDtoList.stream()
            .map(AnimeVoiceActorReq::getId)
            .collect(Collectors.toList());

        List<VoiceActor> voiceActors = voiceActorRepository.findAllById(voiceActorIds);

        // Id와 Part로 구성된 map 생성
        Map<Long, String> voiceActorDtoMap = voiceActorDtoList.stream()
            .collect(Collectors.toMap(AnimeVoiceActorReq::getId, AnimeVoiceActorReq::getPart));

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

    @Override
    public void delete(Long animeId) {
        Anime anime = findAnime(animeId);

        anime.delete();
    }

    @Override
    public StarRatingAvg getStarRatingAverage(Long animeId) {

        Anime anime = findAnime(animeId);

        return new StarRatingAvg(anime.getStarRatingScoreTotal(), anime.getStarRatingCount());
    }

    @Transactional(readOnly = true)
    public Anime findAnime(Long animeId) {
        return animeRepository.findById(animeId).orElseThrow(() -> new NotFoundException("Anime"));
    }
}
