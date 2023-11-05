package io.oduck.api.domain.attractionPoint.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttractionPointResDto {
    @Builder
    @Getter
    public static class IsAttractionPoint{
        private boolean drawing;
        private boolean story;
        private boolean music;
        private boolean character;
        private boolean voiceActor;
    }

}
