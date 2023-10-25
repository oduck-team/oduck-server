package io.oduck.api.domain.voiceActor.service;

import io.oduck.api.domain.voiceActor.dto.VoiceActorRes;
import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import io.oduck.api.domain.voiceActor.repository.VoiceActorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static io.oduck.api.domain.voiceActor.dto.VoiceActorReq.PostReq;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VoiceActorServiceImpl implements VoiceActorService{

    private final VoiceActorRepository voiceActorRepository;

    @Override
    public void save(PostReq req) {
        VoiceActor voiceActor = VoiceActor.builder()
                .name(req.getName())
                .build();
        voiceActorRepository.save(voiceActor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoiceActorRes> getVoiceActors() {
        return voiceActorRepository.findAll().stream()
                .map(va -> new VoiceActorRes(va.getId(), va.getName()))
                .collect(Collectors.toList());
    }
}
