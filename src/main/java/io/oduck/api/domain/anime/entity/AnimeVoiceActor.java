package io.oduck.api.domain.anime.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
public class AnimeVoiceActor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "anime_id")
  private Anime anime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "voice_actor_id")
  private VoiceActor voiceActor;

  @Column(nullable = false, length = 100)
  private String part;
}