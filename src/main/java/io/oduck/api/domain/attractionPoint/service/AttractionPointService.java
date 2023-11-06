package io.oduck.api.domain.attractionPoint.service;

import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.IsAttractionPoint;
import org.springframework.stereotype.Service;

@Service
public interface AttractionPointService {

    //입덕포인트 조회(true/false)
    IsAttractionPoint isAttractionPoint(Long memberId, Long animeId);
}
