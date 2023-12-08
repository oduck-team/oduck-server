package io.oduck.api.domain.attractionPoint.dto;

import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttractionPointStats{
        private double drawing;
        private double story;
        private double music;
        private double character;
        private double voiceActor;
    }
}
