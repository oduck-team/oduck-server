package io.oduck.api.domain.studio.service;

import static io.oduck.api.domain.studio.dto.StudioReq.PostReq;

import io.oduck.api.domain.studio.dto.StudioRes;
import java.util.List;

public interface StudioService {
    void save(PostReq req);

    List<StudioRes> getStudios();

    void update(Long studioId, String name);

    void delete(Long studioId);
}
