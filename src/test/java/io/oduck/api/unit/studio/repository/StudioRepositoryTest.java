package io.oduck.api.unit.studio.repository;

import io.oduck.api.domain.studio.entity.Studio;
import io.oduck.api.domain.studio.repository.StudioRepository;
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
public class StudioRepositoryTest {

    @Autowired
    private StudioRepository studioRepository;

    @Nested
    @DisplayName("등록")
    class SaveStudio {
        @Test
        @DisplayName("스튜디오 등록 성공")
        void saveStudio() {
            //given
            Studio studio = Studio.builder()
                    .name("ufortable")
                    .build();

            Studio savedStudio = studioRepository.save(studio);

            assertThat(studio.getId()).isEqualTo(savedStudio.getId());
            assertThat(studio.getName()).isEqualTo(savedStudio.getName());
        }
    }

    @Nested
    @DisplayName("조회")
    class FindSeries {
        @Test
        @DisplayName("스튜디오 조회 성공")
        void findSeries() {
            //given
            Studio studio = Studio.builder()
                    .name("스튜디오A")
                    .build();

            Studio savedStudio = studioRepository.save(studio);

            //when
            List<Studio> studios = studioRepository.findAll();

            //then
            assertThat(savedStudio).isIn(studios);
        }
    }
}
