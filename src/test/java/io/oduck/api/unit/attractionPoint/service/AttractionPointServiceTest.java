package io.oduck.api.unit.attractionPoint.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.IsAttractionPoint;
import io.oduck.api.domain.attractionPoint.service.AttractionPointServiceStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttractionPointServiceTest {
    @InjectMocks
    private AttractionPointServiceStub attractionPointServiceStub;

    @Nested
    @DisplayName("입덕 포인트 조회")
    class getAttractionPoint{

        @Test
        @DisplayName("입덕 포인트 존재 시 true, false 판별")
        void isAttractionPoint(){
            //given
            Long memberId = 1L;
            Long animeId = 1L;

            //when
            IsAttractionPoint res = attractionPointServiceStub.isAttractionPoint(memberId, animeId);

            //then
            assertDoesNotThrow(() -> attractionPointServiceStub.isAttractionPoint(memberId, animeId));
        }
    }
}
