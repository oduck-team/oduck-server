package io.oduck.api.domain.attractionPoint.controller;

import io.oduck.api.domain.attractionPoint.dto.AttractionPointReqDto.*;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.attractionPoint.service.AttractionPointService;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/attraction-points")
@Slf4j
public class AttractionPointController {

    final AttractionPointService attractionPointService;

    @PostMapping
    public ResponseEntity<?> postAttractionPoint(
            @LoginUser AuthUser user,
            @RequestBody @Valid AttractionPointReq req){
        //TODO: 입덕포인트 작성

        attractionPointService.save(user.getId(), req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{animeId}")
    public ResponseEntity<?> getAttractionPoint(
            @LoginUser AuthUser user,
            @PathVariable("animeId") Long animeId){
        return ResponseEntity.ok(attractionPointService.checkAttractionPoint(user.getId(), animeId));
    }

    @PatchMapping
    public ResponseEntity<?> patchAttractionPoint(
            @LoginUser AuthUser user,
            @RequestBody @Valid AttractionPointReq req){
        boolean update = attractionPointService.update(user.getId(), req);
        return ResponseEntity.status(update? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT).build();
    }

    @GetMapping("/animes/{animeId}")
    public ResponseEntity<?> getAttractionPointStats(
            @PathVariable("animeId") Long animeId){
        //TODO: 애니의 따른 입덕포인트 통계 가져오기
        return ResponseEntity.ok(attractionPointService.getAttractionPointStats(animeId));
    }
}
