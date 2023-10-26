package io.oduck.api.domain.series.service;

import io.oduck.api.domain.series.dto.SeriesRes;

import java.util.List;

import static io.oduck.api.domain.series.dto.SeriesReq.PostReq;

public interface SeriesService {

    void save(PostReq req);

    List<SeriesRes> getSeries();
}
