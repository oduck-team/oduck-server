package io.oduck.api.domain.studio.service;

import io.oduck.api.domain.studio.dto.StudioReq;
import io.oduck.api.domain.studio.dto.StudioRes;
import io.oduck.api.domain.studio.entity.Studio;
import io.oduck.api.domain.studio.repository.StudioRepository;
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
public class StudioServiceImpl implements StudioService{

    private final StudioRepository studioRepository;

    @Override
    public void save(StudioReq.PostReq req) {
        String name = req.getName();

        boolean isExistsName = studioRepository.existsByName(name);

        if(isExistsName == true){
            throw new ConflictException("Studio name");
        }

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
