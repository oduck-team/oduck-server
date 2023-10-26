package io.oduck.api.domain.studio.service;

import io.oduck.api.domain.studio.dto.StudioRes;

import java.util.List;

import static io.oduck.api.domain.studio.dto.StudioReq.PostReq;

public interface StudioService {
    void save(PostReq req);

    List<StudioRes> getStudios();
}
