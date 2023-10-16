package io.oduck.api.domain.voiceActor.repository;

import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceActorRepository extends JpaRepository<VoiceActor, Long> {

}
