package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeVoiceActorRepository extends JpaRepository<AnimeVoiceActor, Long> {

    @Query("select ava from AnimeVoiceActor ava "
        + "join ava.anime "
        + "join ava.voiceActor "
        + "where ava.anime.id = :animeId")
    List<AnimeVoiceActor> findAllByAnimeId(@Param("animeId") Long animeId);
}
