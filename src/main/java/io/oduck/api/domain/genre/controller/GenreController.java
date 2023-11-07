package io.oduck.api.domain.genre.controller;

import io.oduck.api.domain.genre.dto.GenreRes;
import io.oduck.api.domain.genre.service.GenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
@Slf4j
public class GenreController {

    private final GenreService genreService;

    // 장르 조회
    @GetMapping
    public ResponseEntity<Object> getGenres(){

      List<GenreRes> genres = genreService.getGenres();

      return ResponseEntity.ok(genres);
    }
}
