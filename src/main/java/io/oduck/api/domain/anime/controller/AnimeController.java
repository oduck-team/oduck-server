package io.oduck.api.domain.anime.controller;

import io.oduck.api.domain.anime.dto.AnimeRes;
import io.oduck.api.domain.anime.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/animes")
@Slf4j
public class AnimeController {

    private final AnimeService animeService;
    
    // 애니 아이디로 상세 조회
    @GetMapping("/{animeId}")
    public ResponseEntity<Object> getAnimeById(@PathVariable Long animeId){
        
        // TODO: 애니 조회 로직 구현
        AnimeRes res = animeService.getAnimeById(animeId);
        return ResponseEntity
            .ok(res);
    }

    // TODO: 애니 검색 결과 페이징
//    @GetMapping
//    public ResponseEntity<Object> getAnimesBySearchCondition(){
//        return null;
//    }
}
