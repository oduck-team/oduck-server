package io.oduck.api.domain.anime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoiceActorReq {
    private Long id;
    private String part;
}
