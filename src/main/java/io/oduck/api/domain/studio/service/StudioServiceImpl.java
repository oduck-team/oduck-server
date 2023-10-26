package io.oduck.api.domain.studio.service;

import io.oduck.api.domain.studio.dto.StudioReq;
import io.oduck.api.domain.studio.dto.StudioRes;
import io.oduck.api.domain.studio.entity.Studio;
import io.oduck.api.domain.studio.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudioServiceImpl implements StudioService{

    private final StudioRepository studioRepository;

    @Override
    public void save(StudioReq.PostReq req) {
        Studio studio = Studio.builder()
                .name(req.getName())
                .build();
        studioRepository.save(studio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudioRes> getStudios() {
        return studioRepository.findAll().stream()
                .map(st -> new StudioRes(st.getId(), st.getName()))
                .collect(Collectors.toList());
    }
}
