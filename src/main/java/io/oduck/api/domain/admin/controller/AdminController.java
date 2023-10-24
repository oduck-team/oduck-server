package io.oduck.api.domain.admin.controller;

import static io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PostReq;

import io.oduck.api.domain.anime.dto.AnimeReq.PatchGenreIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchOriginalAuthorIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchSeriesIdReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchStudioIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchVoiceActorIdsReq;
import io.oduck.api.domain.anime.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/oduckdmin")
@Slf4j
public class AdminController {

    private final AnimeService animeService;

    //애니 등록
    @PostMapping("/animes")
    public ResponseEntity<Object> postAnime(@RequestBody @Valid PostReq req){

        animeService.save(req);

        return ResponseEntity.ok().build();
    }

    //애니 관련 수정
    @PatchMapping("/animes/{animeId}")
    public ResponseEntity<Object> patchAnime(
        @PathVariable Long animeId, @RequestBody @Valid PatchAnimeReq req
    ){

        animeService.update(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 원작 작가 수정
    @PatchMapping("/animes/{animeId}/original-authors")
    public ResponseEntity<Object> patchAnimeOriginalAuthors(
        @PathVariable Long animeId, @RequestBody PatchOriginalAuthorIdsReq req
    ){
        animeService.updateAnimeOriginalAuthors(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 스튜디오 수정
    @PatchMapping("/animes/{animeId}/studios")
    public ResponseEntity<Object> patchAnimeStudios(
        @PathVariable Long animeId, @RequestBody PatchStudioIdsReq req
    ){

        animeService.updateAnimeStudios(animeId, req);

        return ResponseEntity.noContent().build();
    }
    // 애니의 성우 수정
    @PatchMapping("/animes/{animeId}/voice-actors")
    public ResponseEntity<Object> patchAnimeVoiceActors(
        @PathVariable Long animeId, @RequestBody PatchVoiceActorIdsReq req
    ){
        animeService.updateAnimeVoiceActors(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 장르 수정
    @PatchMapping("/animes/{animeId}/genres")
    public ResponseEntity<Object> patchAnimeGenres(
        @PathVariable Long animeId, @RequestBody PatchGenreIdsReq req
    ){

        animeService.updateAnimeGenres(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 시리즈 수정
    @PatchMapping("/animes/{animeId}/series")
    public ResponseEntity<Object> patchAnimeSeries(
        @PathVariable Long animeId, @RequestBody PatchSeriesIdReq req
    ){

        animeService.updateSeries(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니 삭제
//    @DeleteMapping("/animes/{animeId}")
//    public ResponseEntity<Object> deleteAnime(@PathVariable Long animeId){
//
//        //TODO: 애니 삭제 로직 구현. (애니를 실제로 삭제하지 않음)
//        animeService.delete(animeId);
//        return ResponseEntity.noContent().build();
//    }

//     관리자 애니 조회 (isReleased = false도 조회)
//    @GetMapping("/animes")
//    public ResponseEntity<Object> getAnimes(
//        Pageable pageable, String query, SearchCondition condition
//    ){
//
//        //TODO: 애니 조회 로직 구현.
//        Page<GetAnime> res = adminAnimeService.getAnimes(pageable, query, condition);
//        return ResponseEntity
//            .ok(PageResponse.of(null));
//    }
}
