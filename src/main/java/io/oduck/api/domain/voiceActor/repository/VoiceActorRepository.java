package io.oduck.api.domain.voiceActor.repository;

import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceActorRepository extends JpaRepository<VoiceActor, Long> {

  boolean existsByName(String name);

  List<VoiceActor> findAllByDeletedAtIsNull();

  Optional<VoiceActor> findByIdAndDeletedAtIsNull(Long voiceActorId);
}
