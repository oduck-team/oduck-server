package io.oduck.api.unit.anime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import io.oduck.api.domain.anime.dto.AnimeRes;
import io.oduck.api.domain.anime.service.AnimeServiceStub;
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
    @DisplayName("애니 조회")
    class GetAnime{

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
}
