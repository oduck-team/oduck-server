package io.oduck.api.domain.studio.service;

import io.oduck.api.domain.studio.repository.StudioRepository;
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

}
