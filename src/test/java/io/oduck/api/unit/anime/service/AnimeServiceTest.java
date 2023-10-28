package io.oduck.api.unit.anime.service;

import io.oduck.api.domain.anime.dto.AnimeReq.*;
import io.oduck.api.domain.anime.dto.AnimeVoiceActorReq;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.domain.anime.entity.*;
import io.oduck.api.domain.anime.repository.*;
import io.oduck.api.domain.anime.service.AnimeServiceImpl;
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
import io.oduck.api.global.utils.AnimeTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;
import static io.oduck.api.global.utils.AnimeTestUtils.*;
import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnimeServiceTest {

    @InjectMocks
    private AnimeServiceImpl animeService;

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private OriginalAuthorRepository originalAuthorRepository;

    @Mock
    private AnimeOriginalAuthorRepository animeOriginalAuthorRepository;

    @Mock
    private VoiceActorRepository voiceActorRepository;

    @Mock
    private AnimeVoiceActorRepository animeVoiceActorRepository;

    @Mock
    private StudioRepository studioRepository;

    @Mock
    private AnimeStudioRepository animeStudioRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AnimeGenreRepository animeGenreRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @Nested
    @DisplayName("조회")
    class GetAnime{
        Anime anime = createAnime();

        @Test
        @DisplayName("애니 제목 검색")
        void getAnimes() {
            //given
            String query = null;
            Sort sort = Sort.LATEST;
            OrderDirection order = OrderDirection.DESC;
            int size = 10;
            String cursor = null;

            List<SearchResult> searchResults = new ArrayList<>();

            Slice<SearchResult> slice = new SliceImpl<>(searchResults);

            Pageable pageable = applyPageableForNonOffset(
                    sort.getSort(),
                    order.getOrder(),
                    size
            );

            SearchFilterDsl searchFilter = new SearchFilterDsl(null, null, null, null, null);

            given(animeRepository.findAnimesByCondition(query, cursor, pageable, searchFilter)).willReturn(slice);

            //when
            animeService.getAnimesByCondition(query, cursor, sort, order, size, searchFilter);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findAnimesByCondition(query, cursor, pageable, searchFilter);
        }

        @Test
        @DisplayName("애니 상세 조회")
        void getAnimeById() {
            //given
            Long animeId = 1L;

            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();
            given(animeOriginalAuthorRepository.findAllFetchByAnimeId(animeId)).willReturn(animeOriginalAuthors);

            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            given(animeVoiceActorRepository.findAllFetchByAnimeId(animeId)).willReturn(animeVoiceActors);

            List<AnimeStudio> animeStudios = new ArrayList<>();
            given(animeStudioRepository.findAllFetchByAnimeId(animeId)).willReturn(animeStudios);

            List<AnimeGenre> animeGenres = new ArrayList<>();
            given(animeGenreRepository.findAllFetchByAnimeId(animeId)).willReturn(animeGenres);

            given(animeRepository.findReleasedAnimeById(animeId)).willReturn(Optional.ofNullable(anime));

            //when
            animeService.getAnimeById(animeId);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findReleasedAnimeById(anyLong());
            verify(animeOriginalAuthorRepository, times(1)).findAllFetchByAnimeId(anyLong());
            verify(animeVoiceActorRepository, times(1)).findAllFetchByAnimeId(anyLong());
            verify(animeStudioRepository, times(1)).findAllFetchByAnimeId(anyLong());
            verify(animeGenreRepository, times(1)).findAllFetchByAnimeId(anyLong());
        }
    }

    @Nested
    @DisplayName("등록")
    class SaveAnime{
        List<OriginalAuthor> originalAuthors = getOriginalAuthors();
        List<VoiceActor> voiceActors = getVoiceActors();
        List<Studio> studios = getStudios();
        List<Genre> genres = AnimeTestUtils.getGenres();

        @Test
        @DisplayName("애니 등록 성공")
        void registerAnimeSuccess(){
            //given
            List<Long> originalAuthorIds = getOriginalAuthorIds();
            given(originalAuthorRepository.findAllById(originalAuthorIds)).willReturn(originalAuthors);

            List<Long> voiceActorIds = getVoiceActorIds();
            given(voiceActorRepository.findAllById(voiceActorIds)).willReturn(voiceActors);

            List<Long> studioIds = getStudioIds();
            given(studioRepository.findAllById(studioIds)).willReturn(studios);

            List<Long> genreIds = getGenreIds();
            given(genreRepository.findAllById(genreIds)).willReturn(genres);

            Long seriesId = getSeriesId();
            given(seriesRepository.findById(seriesId)).willReturn(Optional.ofNullable(getSeries()));

            // when
            PostReq req = createPostAnimeRequest();
            animeService.save(req);

            // then
            assertThatNoException();

            //verify
            verify(originalAuthorRepository, times(1)).findAllById(anyList());
            verify(voiceActorRepository, times(1)).findAllById(anyList());
            verify(studioRepository, times(1)).findAllById(anyList());
            verify(genreRepository, times(1)).findAllById(anyList());
            verify(seriesRepository, times(1)).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("수정")
    class UpdateAnime{
        Anime anime = createAnime();
        List<OriginalAuthor> originalAuthors = getOriginalAuthors();
        List<VoiceActor> voiceActors = getVoiceActors();
        List<Studio> studios = getStudios();
        List<Genre> genres = getGenres();
        Series series = getSeries();

        @Test
        @DisplayName("애니 수정 성공")
        void updateAnime(){
            //given
            Long animeId = 1L;
            PatchAnimeReq patchAnimeRequest = createPatchAnimeRequest();

            given(animeRepository.findById(animeId)).willReturn(Optional.ofNullable(anime));

            //when
            animeService.update(animeId, patchAnimeRequest);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("애니의 원작 작가 수정")
        void updateAnimeAuthor(){
            //given
            Long animeId = 1L;

            List originalAuthorIds = getOriginalAuthorIds();
            PatchOriginalAuthorIdsReq patchReq = new PatchOriginalAuthorIdsReq(originalAuthorIds);

            given(animeRepository.findById(animeId)).willReturn(Optional.ofNullable(anime));
            given(originalAuthorRepository.findAllById(originalAuthorIds)).willReturn(originalAuthors);

            //when
            animeService.updateAnimeOriginalAuthors(animeId, patchReq);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findById(anyLong());
            verify(originalAuthorRepository, times(1)).findAllById(anyList());
        }

        @Test
        @DisplayName("애니의 스튜디오 수정")
        void updateAnimeStudios(){
            //given
            Long animeId = 1L;

            List studioIds = getStudioIds();
            PatchStudioIdsReq patchReq = new PatchStudioIdsReq(studioIds);

            given(animeRepository.findById(animeId)).willReturn(Optional.ofNullable(anime));
            given(studioRepository.findAllById(studioIds)).willReturn(studios);

            //when
            animeService.updateAnimeStudios(animeId, patchReq);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findById(anyLong());
            verify(studioRepository, times(1)).findAllById(anyList());
        }

        @Test
        @DisplayName("애니의 성우 수정")
        void updateAnimeVoiceActors(){
            //given
            Long animeId = 1L;

            List<AnimeVoiceActorReq> patchReqs = getVoiceActorReqs();
            List<Long> voiceActorIds = patchReqs.stream().map(AnimeVoiceActorReq::getId)
                .collect(Collectors.toList());

            PatchVoiceActorIdsReq patchReq = new PatchVoiceActorIdsReq(patchReqs);

            given(animeRepository.findById(animeId)).willReturn(Optional.ofNullable(anime));
            given(voiceActorRepository.findAllById(voiceActorIds)).willReturn(voiceActors);

            //when
            animeService.updateAnimeVoiceActors(animeId, patchReq);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findById(anyLong());
            verify(voiceActorRepository, times(1)).findAllById(anyList());
        }

        @Test
        @DisplayName("애니의 장르 수정")
        void updateAnimeGenres(){
            //given
            Long animeId = 1L;

            List genreIds = getGenreIds();
            PatchGenreIdsReq patchReq = new PatchGenreIdsReq(genreIds);

            given(animeRepository.findById(animeId)).willReturn(Optional.ofNullable(anime));
            given(genreRepository.findAllById(genreIds)).willReturn(genres);

            //when
            animeService.updateAnimeGenres(animeId, patchReq);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findById(anyLong());
            verify(genreRepository, times(1)).findAllById(anyList());
        }

        @Test
        @DisplayName("애니의 시리즈 수정")
        void updateSeries(){
            //given
            Long animeId = 1L;

            Long seriesId = getSeriesId();
            PatchSeriesIdReq patchReq = new PatchSeriesIdReq(seriesId);

            given(animeRepository.findById(animeId)).willReturn(Optional.ofNullable(anime));
            given(seriesRepository.findById(seriesId)).willReturn(Optional.ofNullable(series));

            //when
            animeService.updateSeries(animeId, patchReq);

            //then
            assertThatNoException();

            //verify
            verify(animeRepository, times(1)).findById(anyLong());
            verify(seriesRepository, times(1)).findById(anyLong());
        }
    }
}
