package io.oduck.api.domain.attractionPoint.service;


import io.oduck.api.domain.attractionPoint.dto.AttractionPointReqDto.*;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.*;
import org.springframework.stereotype.Service;

@Service
public interface AttractionPointService {

    //입덕 포인트 추가
    void save(Long memberId, AttractionPointReq req);

    //입덕 포인트 평가 여부
    CheckAttractionPoint checkAttractionPoint(Long memberId, Long animeId);

    //입덕포인트 조회(true/false)
    IsAttractionPoint isAttractionPoint(Long memberId, Long animeId);

    boolean update(Long memberId, Long attractionPointId, UpdateAttractionPoint req);

    AttractionPointStats getAttractionPointStats(Long anime);
}
