package io.oduck.api.domain.series.service;

import static io.oduck.api.domain.series.dto.SeriesReq.PostReq;

import io.oduck.api.domain.series.dto.SeriesRes;
import io.oduck.api.domain.series.entity.Series;
import io.oduck.api.domain.series.repository.SeriesRepository;
import io.oduck.api.global.exception.ConflictException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SeriesServiceImpl implements SeriesService{

    private final SeriesRepository seriesRepository;

    @Override
    public void save(PostReq req) {
        String title = req.getTitle();

        boolean isExistsTitle = seriesRepository.existsByTitle(title);

        if(isExistsTitle == true){
            throw new ConflictException("Series Title");
        }

        Series series = Series.builder()
                .title(req.getTitle())
                .build();
        seriesRepository.save(series);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeriesRes> getSeries() {
        return seriesRepository.findAll().stream()
                .map(s -> new SeriesRes(s.getId(), s.getTitle()))
                .collect(Collectors.toList());
    }
}
