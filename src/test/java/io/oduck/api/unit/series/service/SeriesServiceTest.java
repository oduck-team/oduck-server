package io.oduck.api.unit.series.service;

import io.oduck.api.domain.series.dto.SeriesReq;
import io.oduck.api.domain.series.entity.Series;
import io.oduck.api.domain.series.repository.SeriesRepository;
import io.oduck.api.domain.series.service.SeriesServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SeriesServiceTest {
    @InjectMocks
    private SeriesServiceImpl seriesService;

    @Mock
    private SeriesRepository seriesRepository;

    @Nested
    @DisplayName("등록")
    class SaveSeries {
        @Test
        @DisplayName("시리즈 등록 성공")
        void saveSeriesSuccess() {
            //given
            SeriesReq.PostReq postReq = new SeriesReq.PostReq("스파이 패밀리");

            //when
            seriesService.save(postReq);

            //then
            assertThatNoException();

            //verify
            verify(seriesRepository, times(1)).save(any(Series.class));
        }
    }

    @Nested
    @DisplayName("조회")
    class GetSeries {
        @Test
        @DisplayName("시리즈 조회 성공")
        void getSeries() {
            //when
            seriesService.getSeries();

            //then
            assertThatNoException();

            //verify
            verify(seriesRepository, times(1)).findAll();
        }
    }
}
