package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeVoiceActorRepository extends JpaRepository<AnimeVoiceActor, Long> {

    @Query("select distinct ava from AnimeVoiceActor ava "
        + "join fetch ava.anime a "
        + "join fetch ava.voiceActor va "
        + "where ava.anime.id = :animeId")
    List<AnimeVoiceActor> findAllFetchByAnimeId(@Param("animeId") Long animeId);
}
