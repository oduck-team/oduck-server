package io.oduck.api.domain.attractionPoint.dto;

import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class AttractionPointReqDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttractionPointReq{
        private Long animeId;
        @NotNull(message = "입덕포인트를 선택하세요.")
        List<AttractionElement> attractionElements;
    }
}
