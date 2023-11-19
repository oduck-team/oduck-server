package io.oduck.api.domain.attractionPoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckAttractionPoint{
        private Boolean isAttractionPoint;
    }
}
