package io.oduck.api.domain.series.service;

import static io.oduck.api.domain.series.dto.SeriesReq.PostReq;

import io.oduck.api.domain.series.dto.SeriesRes;
import java.util.List;

public interface SeriesService {

    void save(PostReq req);

    List<SeriesRes> getSeries();

    void update(Long seriesId, String title);

    void delete(Long seriesId);
}
