package io.oduck.api.domain.voiceActor.repository;

import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoiceActorRepository extends JpaRepository<VoiceActor, Long>, VoiceActorRepositoryCustom {

  List<VoiceActor> findAllByDeletedAtIsNull();

  @Query("select distinct va from VoiceActor va left join fetch va.animeVoiceActors where va.id = :id and va.deletedAt = null")
  Optional<VoiceActor> findByIdAndDeletedAtIsNull(@Param("id") Long voiceActorId);
}
