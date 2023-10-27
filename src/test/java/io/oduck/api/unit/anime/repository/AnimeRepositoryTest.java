package io.oduck.api.unit.anime.repository;

import io.oduck.api.domain.anime.dto.AnimeReq;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.domain.anime.entity.*;
import io.oduck.api.domain.anime.repository.*;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;
import static io.oduck.api.global.utils.AnimeTestUtils.*;
import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnimeOriginalAuthorRepository animeOriginalAuthorRepository;

    @Autowired
    private OriginalAuthorRepository originalAuthorRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private AnimeStudioRepository animeStudioRepository;

    @Autowired
    private VoiceActorRepository voiceActorRepository;

    @Autowired
    private AnimeVoiceActorRepository animeVoiceActorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AnimeGenreRepository animeGenreRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Nested
    @DisplayName("등록")
    class PostAnime {

        @Test
        @DisplayName("연관 관계 설정 성공")
        void saveAnime() {
            /**
             * 원작 작가 연관 관계 설정
             */
            // 1. OriginalAuthor 생성 size=1
            String originalAuthorName = "엔도 타츠야";
            OriginalAuthor originalAuthor = OriginalAuthor.builder()
                .name(originalAuthorName)
                .build();
            originalAuthorRepository.save(originalAuthor);

            // 2. AnimeOriginalAuthor 생성
            AnimeOriginalAuthor animeOriginalAuthor = AnimeOriginalAuthor.createAnimeOriginalAuthor(
                originalAuthor);

            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();
            animeOriginalAuthors.add(animeOriginalAuthor);

            /**
             * 스튜디오 생성
             */
            // 1. Studio 생성 size=1
            String studioName = "ufortable";
            Studio studio = Studio.builder()
                .name(studioName)
                .build();
            studioRepository.save(studio);

            assertThat(studio.getName()).isEqualTo(studioName);

            // 2. AnimeStudio 생성
            AnimeStudio animeStudio = AnimeStudio.createAnimeStudio(studio);

            List<AnimeStudio> animeStudios = new ArrayList<>();
            animeStudios.add(animeStudio);

            /**
             * 성우 생성
             */
            // 1. VoiceActor 생성 size=5
            int voiceActorSize = 5;

            List<VoiceActor> voiceActors = new ArrayList<>();
            for (int i = 0; i < voiceActorSize; i++) {
                VoiceActor voiceActor = VoiceActor.builder()
                    .name("성우" + i)
                    .build();
                voiceActors.add(voiceActor);
            }

            voiceActorRepository.saveAll(voiceActors);

            // 2. AnimeVoiceActor 생성
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            for (VoiceActor voiceActor : voiceActors) {
                AnimeVoiceActor animeVoiceActor = AnimeVoiceActor.createAnimeVoiceActor("파트",
                    voiceActor);
                animeVoiceActors.add(animeVoiceActor);
            }

            /**
             * 장르 생성
             */
            // 1. Genre 생성 size = 2
            int genreSize = 2;

            List<Genre> genres = new ArrayList<>();
            for (int i = 0; i < genreSize; i++) {
                Genre genre = Genre.builder()
                    .name("장르" + i)
                    .build();
                genres.add(genre);
            }

            genreRepository.saveAll(genres);

            // 2. AnimeGenre 생성
            List<AnimeGenre> animeGenres = new ArrayList<>();
            for (Genre genre : genres) {
                AnimeGenre animeGenre = AnimeGenre.createAnimeGenre(genre);
                animeGenres.add(animeGenre);
            }

            /**
             * 시리즈 생성
             */
            // 1. Series
            String seriesTitle = "귀멸의 칼날";
            Series series = Series.builder()
                .title(seriesTitle)
                .build();

            // 2. Series 생성
            seriesRepository.save(series);

            // 애니 생성
            Anime anime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), animeOriginalAuthors,
                animeStudios, animeVoiceActors, animeGenres, series
            );

            Anime savedAnime = animeRepository.save(anime);

            assertThat(savedAnime.getTitle()).isEqualTo(getTitle());
            assertThat(savedAnime.getSummary()).isEqualTo(getSummary());
            assertThat(savedAnime.getBroadcastType()).isEqualTo(getBroadcastType());
            assertThat(savedAnime.getEpisodeCount()).isEqualTo(getEpisodeCount());
            assertThat(savedAnime.getThumbnail()).isEqualTo(getThumbnail());
            assertThat(savedAnime.getYear()).isEqualTo(getYear());
            assertThat(savedAnime.getQuarter()).isEqualTo(getQuarter());
            assertThat(savedAnime.getRating()).isEqualTo(getRating());
            assertThat(savedAnime.getStatus()).isEqualTo(getStatus());
            assertThat(savedAnime.isReleased()).isFalse();
            assertThat(savedAnime.getViewCount()).isEqualTo(0);
            assertThat(savedAnime.getReviewCount()).isEqualTo(0);
            assertThat(savedAnime.getBookmarkCount()).isEqualTo(0);
            assertThat(savedAnime.getStarRatingScoreTotal()).isEqualTo(0);

            assertThat(savedAnime.getAnimeOriginalAuthors().size()).isEqualTo(1);
            assertThat(savedAnime.getAnimeStudios().size()).isEqualTo(1);
            assertThat(savedAnime.getAnimeVoiceActors().size()).isEqualTo(voiceActorSize);
            assertThat(savedAnime.getAnimeGenres().size()).isEqualTo(genreSize);
            assertThat(savedAnime.getSeries().getTitle()).isEqualTo(seriesTitle);
        }

        @Test
        @DisplayName("연관 관계 설정 시 연결 테이블의 값이 없을 때")
        void saveAnimeNoAnimeOriginalAuthor() {
            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();
            List<AnimeStudio> animeStudios = new ArrayList<>();
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            List<AnimeGenre> animeGenres = new ArrayList<>();

            Anime anime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), animeOriginalAuthors,
                animeStudios, animeVoiceActors, animeGenres, null
            );

            Anime savedAnime = animeRepository.save(anime);
            assertThat(savedAnime.getAnimeOriginalAuthors().isEmpty()).isTrue();
            assertThat(savedAnime.getAnimeStudios().isEmpty()).isTrue();
            assertThat(savedAnime.getAnimeVoiceActors().isEmpty()).isTrue();
            assertThat(savedAnime.getAnimeGenres().isEmpty()).isTrue();
            assertThat(savedAnime.getSeries()).isNull();
        }
    }

    @Nested
    @DisplayName("수정")
    class PatchAnime {

        @Test
        @DisplayName("애니 원작 작가 수정 성공")
        void changeAnimeOriginalAuthors() {
            // given
            // 초기 원작 작가 설정
            String originalAuthorName = "작가";
            int firstInsertSize = 2;

            List<OriginalAuthor> originalAuthors = new ArrayList<>();
            for (int i = 0; i < firstInsertSize; i++) {
                OriginalAuthor originalAuthor = OriginalAuthor.builder()
                    .name(originalAuthorName)
                    .build();
                originalAuthors.add(originalAuthor);
            }

            originalAuthorRepository.saveAll(originalAuthors);

            // 원작 작가와 애니 연관 관계 테이블 연관 관계 맺기
            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();
            for (OriginalAuthor originalAuthor : originalAuthors) {
                AnimeOriginalAuthor animeOriginalAuthor = AnimeOriginalAuthor.createAnimeOriginalAuthor(
                    originalAuthor);
                animeOriginalAuthors.add(animeOriginalAuthor);
            }

            List<AnimeStudio> animeStudios = new ArrayList<>();
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            List<AnimeGenre> animeGenres = new ArrayList<>();

            // 애니 생성
            Anime anime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), animeOriginalAuthors,
                animeStudios, animeVoiceActors, animeGenres, null
            );

            Long savedAnimeId = animeRepository.saveAndFlush(anime).getId();

            //when
            //업데이트할 작가 생성
            String updatingOriginalAuthorName = "유재석";
            OriginalAuthor updatingOriginalAuthor = OriginalAuthor.builder()
                .name(updatingOriginalAuthorName)
                .build();
            originalAuthorRepository.save(updatingOriginalAuthor);

            List<AnimeOriginalAuthor> updatingAnimeOriginalAuthors = new ArrayList<>();
            AnimeOriginalAuthor updatingAnimeOriginalAuthor = AnimeOriginalAuthor.createAnimeOriginalAuthor(
                updatingOriginalAuthor);
            updatingAnimeOriginalAuthors.add(updatingAnimeOriginalAuthor);

            // 애니 찾기
            Anime findAnime = animeRepository.findById(savedAnimeId).get();

            // 애니 수정
            findAnime.updateAnimeOriginalAuthors(updatingAnimeOriginalAuthors);

            Long findAnimeId = findAnime.getId();
            List<AnimeOriginalAuthor> findAnimeOriginalAuthors = animeOriginalAuthorRepository.findAllFetchByAnimeId(findAnimeId);

            // then
            String findOriginalAuthorName = findAnime.getAnimeOriginalAuthors().get(0)
                .getOriginalAuthor().getName();
            assertThat(findOriginalAuthorName).isEqualTo(updatingOriginalAuthorName);
            assertThat(findAnimeOriginalAuthors.size()).isNotEqualTo(firstInsertSize);
            assertThat(findAnimeOriginalAuthors.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("애니 스튜디오 수정 성공")
        void changeAnimeStudios() {
            // given
            // 초기 스튜디오 설정
            String studioName = "스튜디오";
            int firstInsertSize = 2;

            List<Studio> studios = new ArrayList<>();
            for (int i = 0; i < firstInsertSize; i++) {
                Studio studio = Studio.builder()
                    .name(studioName)
                    .build();
                studios.add(studio);
            }

            studioRepository.saveAll(studios);

            // 스튜디오와 애니 연관 관계 테이블 연관 관계 맺기
            List<AnimeStudio> animeStudios = new ArrayList<>();
            for (Studio studio : studios) {
                AnimeStudio animeStudio = AnimeStudio.createAnimeStudio(studio);
                animeStudios.add(animeStudio);
            }

            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            List<AnimeGenre> animeGenres = new ArrayList<>();

            // 애니 생성
            Anime anime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), animeOriginalAuthors,
                animeStudios, animeVoiceActors, animeGenres, null
            );

            Long savedAnimeId = animeRepository.saveAndFlush(anime).getId();

            //when
            //업데이트할 스튜디오 생성
            String updatingStudioName = "업데이트 스튜디오";
            Studio updatingStudio = Studio.builder()
                .name(updatingStudioName)
                .build();
            studioRepository.save(updatingStudio);

            List<AnimeStudio> updatingAnimeStudios = new ArrayList<>();
            AnimeStudio animeStudio = AnimeStudio.createAnimeStudio(updatingStudio);
            updatingAnimeStudios.add(animeStudio);

            // 애니 찾기
            Anime findAnime = animeRepository.findById(savedAnimeId).get();

            // 애니 수정
            findAnime.updateAnimeStudios(updatingAnimeStudios);

            Long findAnimeId = findAnime.getId();
            List<AnimeStudio> findAnimeOriginalAuthors = animeStudioRepository.findAllFetchByAnimeId(findAnimeId);

            // then
            String findStudioName = findAnime.getAnimeStudios().get(0).getStudio().getName();
            assertThat(findStudioName).isEqualTo(updatingStudioName);
            assertThat(findAnimeOriginalAuthors.size()).isNotEqualTo(firstInsertSize);
            assertThat(findAnimeOriginalAuthors.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("애니 성우 수정 성공")
        void changeAnimeVoiceActors() {
            // given
            // 초기 성우 설정
            String voiceActorName = "성우";
            int firstInsertSize = 2;

            List<VoiceActor> voiceActors = new ArrayList<>();
            for (int i = 0; i < firstInsertSize; i++) {
                VoiceActor voiceActor = VoiceActor.builder()
                    .name(voiceActorName)
                    .build();
                voiceActors.add(voiceActor);
            }

            voiceActorRepository.saveAll(voiceActors);

            // 성우와 애니 연관 관계 테이블 연관 관계 맺기
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();
            for (VoiceActor voiceActor : voiceActors) {
                AnimeVoiceActor animeVoiceActor = AnimeVoiceActor.createAnimeVoiceActor("part",
                    voiceActor);
                animeVoiceActors.add(animeVoiceActor);
            }

            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();
            List<AnimeStudio> animeStudios = new ArrayList<>();
            List<AnimeGenre> animeGenres = new ArrayList<>();

            // 애니 생성
            Anime anime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), animeOriginalAuthors,
                animeStudios, animeVoiceActors, animeGenres, null
            );

            Long savedAnimeId = animeRepository.saveAndFlush(anime).getId();

            //when
            //업데이트할 성우 생성
            String updatingVoiceActorName = "성우";
            VoiceActor updatingVoiceActor = VoiceActor.builder()
                .name(updatingVoiceActorName)
                .build();
            voiceActorRepository.save(updatingVoiceActor);

            List<AnimeVoiceActor> updatingAnimeVoiceActors = new ArrayList<>();
            AnimeVoiceActor animeVoiceActor = AnimeVoiceActor.createAnimeVoiceActor("part",
                updatingVoiceActor);
            updatingAnimeVoiceActors.add(animeVoiceActor);

            // 애니 찾기
            Anime findAnime = animeRepository.findById(savedAnimeId).get();

            // 애니 수정
            findAnime.updateAnimeVoiceActors(updatingAnimeVoiceActors);

            Long findAnimeId = findAnime.getId();
            List<AnimeVoiceActor> findAnimeVoiceActors = animeVoiceActorRepository.findAllFetchByAnimeId(findAnimeId);

            // then
            String firstVoiceActorName = findAnime.getAnimeVoiceActors().get(0).getVoiceActor()
                .getName();
            assertThat(firstVoiceActorName).isEqualTo(updatingVoiceActorName);
            assertThat(findAnimeVoiceActors.size()).isNotEqualTo(firstInsertSize);
            assertThat(findAnimeVoiceActors.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("애니 장르 수정 성공")
        void changeAnimeGenres() {
            // given
            // 초기 장르 설정
            String genreName = "장르";
            int firstInsertSize = 2;

            List<Genre> genres = new ArrayList<>();
            for (int i = 0; i < firstInsertSize; i++) {
                Genre genre = Genre.builder()
                    .name(genreName)
                    .build();
                genres.add(genre);
            }

            genreRepository.saveAll(genres);

            // 장르와 애니 연관 관계 테이블 연관 관계 맺기
            List<AnimeGenre> animeGenres = new ArrayList<>();
            for (Genre genre : genres) {
                AnimeGenre animeGenre = AnimeGenre.createAnimeGenre(genre);
                animeGenres.add(animeGenre);
            }

            List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();
            List<AnimeStudio> animeStudios = new ArrayList<>();
            List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();

            // 애니 생성
            Anime anime = Anime.createAnime(
                getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(), getThumbnail(),
                getYear(), getQuarter(), getRating(), getStatus(), animeOriginalAuthors,
                animeStudios, animeVoiceActors, animeGenres, null
            );

            Long savedAnimeId = animeRepository.saveAndFlush(anime).getId();

            //when
            //업데이트할 장르 생성
            String updatingGenreName = "판타지";
            Genre updatingGenre = Genre.builder()
                .name(updatingGenreName)
                .build();
            genreRepository.save(updatingGenre);

            List<AnimeGenre> updatingAnimeGenres = new ArrayList<>();
            AnimeGenre animeGenre = AnimeGenre.createAnimeGenre(updatingGenre);
            updatingAnimeGenres.add(animeGenre);

            // 애니 찾기
            Anime findAnime = animeRepository.findById(savedAnimeId).get();

            // 애니 수정
            findAnime.updateAnimeGenre(updatingAnimeGenres);

            Long findAnimeId = findAnime.getId();
            List<AnimeGenre> findAnimeGenres = animeGenreRepository.findAllFetchByAnimeId(findAnimeId);

            // then
            String firstGenreName = findAnime.getAnimeGenres().get(0).getGenre().getName();
            assertThat(firstGenreName).isEqualTo(updatingGenreName);
            assertThat(findAnimeGenres.size()).isNotEqualTo(firstInsertSize);
            assertThat(findAnimeGenres.size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("조회")
    class GetAnime{
        @Test
        @DisplayName("애니 조회 성공")
        void getAnimes() {
            //given
            String query = null;
            AnimeReq.Sort sort = AnimeReq.Sort.LATEST;
            OrderDirection order = OrderDirection.DESC;
            int size = 10;
            String cursor = null;

            Pageable pageable = applyPageableForNonOffset(
                    sort.getSort(),
                    order.getOrder(),
                    size
            );

            SearchFilterDsl searchFilter = new SearchFilterDsl(null, null, null, null, null);

            //when
            Slice<SearchResult> animes = animeRepository.findAnimesByCondition(
                    query, cursor, pageable, searchFilter
            );

            //then
            assertThat(animes).isNotNull();
            assertThat(animes.getContent().get(0).getId()).isNotNull();
            assertThat(animes.getContent().get(0).getStarScoreAvg()).isNotNull();
            assertThat(animes.getContent().get(0).getThumbnail()).isNotNull();
            assertThat(animes.getContent().get(0).getStarScoreAvg()).isNotNull();
        }

        @Test
        @DisplayName("애니 상세 조회 성공")
        void getAnimeById(){
            //given
            Long animeId = 1L;

            //when
            Optional<Anime> optionalAnime = animeRepository.findById(animeId);

            //then
            assertThat(optionalAnime.isPresent()).isTrue();
        }
    }
}