package io.oduck.api.unit.anime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import io.oduck.api.domain.anime.dto.AnimeReq.PostReq;
import io.oduck.api.domain.anime.dto.AnimeRes;
import io.oduck.api.domain.anime.service.AnimeServiceStub;
import io.oduck.api.global.utils.AnimeTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnimeServiceTest {

    @InjectMocks
    private AnimeServiceStub animeService;

    @Nested
    @DisplayName("조회")
    class findAnime{
        @Test
        @DisplayName("애니 상세 조회")
        void getAnimeById() {
            //given
            Long animeId = 1L;

            //when
            AnimeRes response = animeService.getAnimeById(animeId);

            //then
            assertThat(response.getAnime().getId()).isEqualTo(animeId);
            assertThatNoException();
        }
    }

    @Nested
    @DisplayName("등록")
    class SaveAnime{
        Anime anime = createAnime();
        List<OriginalAuthor> originalAuthors = getOriginalAuthors();
        List<VoiceActor> voiceActors = getVoiceActors();
        List<Studio> studios = getStudios();
        List<Genre> genres = AnimeTestUtils.getGenres();

        @Test
        @DisplayName("애니 등록 성공")
        void postAnime(){
            PostReq req = AnimeTestUtils.createPostAnimeRequest();

            Long animeId = animeService.save(req);

            assertThat(animeId).isEqualTo(1L);
        }
    }
}
