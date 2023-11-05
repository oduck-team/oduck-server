package io.oduck.api.domain.voiceActor.service;

import io.oduck.api.domain.voiceActor.dto.VoiceActorRes;

import java.util.List;

import static io.oduck.api.domain.voiceActor.dto.VoiceActorReq.PostReq;

public interface VoiceActorService {

    void save(PostReq req);

    List<VoiceActorRes> getVoiceActors();

    void update(Long voiceActorId, String name);

    void delete(Long voiceActorId);
}
