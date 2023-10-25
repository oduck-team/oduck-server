package io.oduck.api.unit.series.repository;

import io.oduck.api.domain.series.entity.Series;
import io.oduck.api.domain.series.repository.SeriesRepository;
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
public class SeriesRepositoryTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @Nested
    @DisplayName("등록")
    class SaveSeries {
        @Test
        @DisplayName("시리즈 등록 성공")
        void saveSeries() {
            //given
            Series series = Series.builder()
                    .title("스파이 패밀리")
                    .build();

            //when
            Series savedSeries = seriesRepository.save(series);

            //then
            assertThat(series.getId()).isEqualTo(savedSeries.getId());
            assertThat(series.getTitle()).isEqualTo(savedSeries.getTitle());
        }
    }

    @Nested
    @DisplayName("조회")
    class FindSeries {
        @Test
        @DisplayName("시리즈 조회 성공")
        void findSeries() {
            //given
            Series series = Series.builder()
                    .title("스파이 패밀리")
                    .build();

            Series savedSeries = seriesRepository.save(series);

            //when
            List<Series> originalAuthors = seriesRepository.findAll();

            //then
            assertThat(savedSeries).isIn(originalAuthors);
        }
    }
}
