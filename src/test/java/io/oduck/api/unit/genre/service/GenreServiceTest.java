package io.oduck.api.unit.genre.service;

import io.oduck.api.domain.genre.dto.GenreReq;
import io.oduck.api.domain.genre.entity.Genre;
import io.oduck.api.domain.genre.repository.GenreRepository;
import io.oduck.api.domain.genre.service.GenreServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @InjectMocks
    private GenreServiceImpl genreService;

    @Mock
    private GenreRepository genreRepository;

    @Nested
    @DisplayName("등록")
    class SaveGenre {
        @Test
        @DisplayName("장르 등록 성공")
        void saveVoiceActors() {
            //given
            GenreReq.PostReq postReq = new GenreReq.PostReq("로맨스");

            //when
            genreService.save(postReq);

            //then
            assertThatNoException();

            //verify
            verify(genreRepository, times(1)).save(any(Genre.class));
        }
    }

    @Nested
    @DisplayName("조회")
    class GetVoiceActors {
        @Test
        @DisplayName("장르 조회 성공")
        void getVoiceActors() {
            //when
            genreService.getGenres();

            //then
            assertThatNoException();

            //verify
            verify(genreRepository, times(1)).findAllByDeletedAtIsNull();
        }
    }
}
