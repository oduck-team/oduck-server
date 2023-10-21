package io.oduck.api.domain.voiceActor.service;

import io.oduck.api.domain.voiceActor.repository.VoiceActorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VoiceActorServiceImpl implements VoiceActorService{

    private final VoiceActorRepository voiceActorRepository;
}
