package io.oduck.api.domain.voiceActor.service;

import static io.oduck.api.domain.voiceActor.dto.VoiceActorReq.PostReq;

import io.oduck.api.domain.voiceActor.dto.VoiceActorRes;
import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import io.oduck.api.domain.voiceActor.repository.VoiceActorRepository;
import io.oduck.api.global.exception.ConflictException;
import io.oduck.api.global.exception.NotFoundException;
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
public class VoiceActorServiceImpl implements VoiceActorService{

    private final VoiceActorRepository voiceActorRepository;

    @Override
    public void save(PostReq req) {
        String name = req.getName();

        boolean isExistsName = voiceActorRepository.existsByName(name);

        if(isExistsName == true){
            throw new ConflictException("Studio name");
        }

        VoiceActor voiceActor = VoiceActor.builder()
                .name(req.getName())
                .build();
        voiceActorRepository.save(voiceActor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoiceActorRes> getVoiceActors() {
        return voiceActorRepository.findAllByDeletedAtIsNull().stream()
                .map(va -> new VoiceActorRes(va.getId(), va.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long voiceActorId, String name) {
        VoiceActor voiceActor = findById(voiceActorId);

        voiceActor.update(name);
    }

    @Override
    public void delete(Long voiceActorId) {
        VoiceActor voiceActor = findById(voiceActorId);

        voiceActor.delete();
    }

    private VoiceActor findById(Long voiceActorId) {
        return voiceActorRepository.findByIdAndDeletedAtIsNull(voiceActorId)
            .orElseThrow(() -> new NotFoundException("voiceActor"));
    }
}
