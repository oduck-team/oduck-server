package io.oduck.api.unit.genre.repository;

import io.oduck.api.domain.genre.entity.Genre;
import io.oduck.api.domain.genre.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Nested
    @DisplayName("등록")
    class SaveGenre {
        @Test
        @DisplayName("장르 등록 성공")
        void saveGenre() {
            //given
            Genre genre = Genre.builder()
                    .name("로맨스")
                    .build();

            //when
            Genre savedGenre = genreRepository.save(genre);

            //then
            assertThat(genre.getId()).isEqualTo(savedGenre.getId());
            assertThat(genre.getName()).isEqualTo(savedGenre.getName());
        }
    }

    @Nested
    @DisplayName("조회")
    class FindGenre {
        @Test
        @DisplayName("장르 조회 성공")
        void findGenre() {
            //given
            Genre genre = Genre.builder()
                    .name("판타지")
                    .build();

            Genre savedGenre = genreRepository.save(genre);

            //when
            List<Genre> genres = genreRepository.findAll();

            //then
            assertThat(savedGenre).isIn(genres);

        }
    }
}
