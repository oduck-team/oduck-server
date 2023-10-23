package io.oduck.api.unit.originalAuthor.repository;

import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import io.oduck.api.domain.originalAuthor.repository.OriginalAuthorRepository;
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
public class OriginalAuthorRepositoryTest {

    @Autowired
    private OriginalAuthorRepository originalAuthorRepository;

    @Nested
    @DisplayName("등록")
    class SaveOriginalAuthorTest{
        @Test
        @DisplayName("원작 작가 등록 성공")
        void SaveOriginalAuthor() {
            //given
            OriginalAuthor originalAuthor = OriginalAuthor.builder()
                    .name("엔도 타츠야")
                    .build();

            //when
            OriginalAuthor savedOriginalAuthor = originalAuthorRepository.save(originalAuthor);

            //then
            assertThat(originalAuthor.getId()).isEqualTo(savedOriginalAuthor.getId());
            assertThat(originalAuthor.getName()).isEqualTo(savedOriginalAuthor.getName());
        }
    }

    @Nested
    @DisplayName("조회")
    class FindOriginalAuthor {
        @Test
        @DisplayName("원작 작가 조회 성공")
        void findOriginalAuthor() {
            //given
            OriginalAuthor originalAuthor = OriginalAuthor.builder()
                    .name("성우A")
                    .build();

            OriginalAuthor savedOriginalAuthor = originalAuthorRepository.save(originalAuthor);

            //when
            List<OriginalAuthor> originalAuthors = originalAuthorRepository.findAll();

            //then
            assertThat(savedOriginalAuthor).isIn(originalAuthors);

        }
    }
}
