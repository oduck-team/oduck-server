package io.oduck.api.domain.admin.controller;

import io.oduck.api.domain.anime.dto.AnimeReq;
import io.oduck.api.domain.anime.service.AnimeService;
import io.oduck.api.domain.genre.dto.GenreReq;
import io.oduck.api.domain.genre.dto.GenreRes;
import io.oduck.api.domain.genre.service.GenreService;
import io.oduck.api.domain.originalAuthor.dto.OriginalAuthorReq;
import io.oduck.api.domain.originalAuthor.dto.OriginalAuthorRes;
import io.oduck.api.domain.originalAuthor.service.OriginalAuthorService;
import io.oduck.api.domain.series.dto.SeriesReq;
import io.oduck.api.domain.series.dto.SeriesRes;
import io.oduck.api.domain.series.service.SeriesService;
import io.oduck.api.domain.studio.dto.StudioReq;
import io.oduck.api.domain.studio.dto.StudioRes;
import io.oduck.api.domain.studio.service.StudioService;
import io.oduck.api.domain.voiceActor.dto.VoiceActorReq;
import io.oduck.api.domain.voiceActor.dto.VoiceActorRes;
import io.oduck.api.domain.voiceActor.service.VoiceActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/oduckdmin")
@Slf4j
public class AdminController {

    private final AnimeService animeService;
    private final OriginalAuthorService originalAuthorService;
    private final VoiceActorService voiceActorService;
    private final StudioService studioService;
    private final GenreService genreService;
    private final SeriesService seriesService;

    //애니 등록
    @PostMapping("/animes")
    public ResponseEntity<Object> postAnime(@RequestBody @Valid AnimeReq.PostReq req){

        animeService.save(req);

        return ResponseEntity.ok().build();
    }

    //애니 관련 수정
    @PatchMapping("/animes/{animeId}")
    public ResponseEntity<Object> patchAnime(
        @PathVariable Long animeId, @RequestBody @Valid AnimeReq.PatchAnimeReq req
    ){

        animeService.update(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 원작 작가 수정
    @PatchMapping("/animes/{animeId}/original-authors")
    public ResponseEntity<Object> patchAnimeOriginalAuthors(
        @PathVariable Long animeId, @RequestBody AnimeReq.PatchOriginalAuthorIdsReq req
    ){
        animeService.updateAnimeOriginalAuthors(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 스튜디오 수정
    @PatchMapping("/animes/{animeId}/studios")
    public ResponseEntity<Object> patchAnimeStudios(
        @PathVariable Long animeId, @RequestBody AnimeReq.PatchStudioIdsReq req
    ){

        animeService.updateAnimeStudios(animeId, req);

        return ResponseEntity.noContent().build();
    }
    // 애니의 성우 수정
    @PatchMapping("/animes/{animeId}/voice-actors")
    public ResponseEntity<Object> patchAnimeVoiceActors(
        @PathVariable Long animeId, @RequestBody AnimeReq.PatchVoiceActorIdsReq req
    ){
        animeService.updateAnimeVoiceActors(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 장르 수정
    @PatchMapping("/animes/{animeId}/genres")
    public ResponseEntity<Object> patchAnimeGenres(
        @PathVariable Long animeId, @RequestBody AnimeReq.PatchGenreIdsReq req
    ){

        animeService.updateAnimeGenres(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 시리즈 수정
    @PatchMapping("/animes/{animeId}/series")
    public ResponseEntity<Object> patchAnimeSeries(
        @PathVariable Long animeId, @RequestBody AnimeReq.PatchSeriesIdReq req
    ){

        animeService.updateSeries(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니 삭제
    @DeleteMapping("/animes/{animeId}")
    public ResponseEntity<Object> deleteAnime(@PathVariable Long animeId) {

        animeService.delete(animeId);

        return ResponseEntity.noContent().build();
    }

    // 관리자 애니 조회 (isReleased = false도 조회)
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

    @GetMapping("/original-authors")
    public ResponseEntity<Object> getOriginalAuthors(){

        List<OriginalAuthorRes> originalAuthors = originalAuthorService.getOriginalAuthors();

        return ResponseEntity.ok(originalAuthors);
    }

    @PostMapping("/original-authors")
    public ResponseEntity<Object> postOriginalAuthor(@RequestBody @Valid OriginalAuthorReq.PostReq req) {

        originalAuthorService.save(req);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/voice-actors")
    public ResponseEntity<Object> getVoiceActors() {

        List<VoiceActorRes> voiceActors = voiceActorService.getVoiceActors();

        return ResponseEntity.ok(voiceActors);
    }

    @PostMapping("/voice-actors")
    public ResponseEntity<Object> postVoiceActor(@RequestBody @Valid VoiceActorReq.PostReq req) {

        voiceActorService.save(req);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/studios")
    public ResponseEntity<Object> getStudios() {

        List<StudioRes> studios = studioService.getStudios();

        return ResponseEntity.ok(studios);
    }

    @PostMapping("/studios")
    public ResponseEntity<Object> postStudio(@RequestBody @Valid StudioReq.PostReq req) {

        studioService.save(req);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genres")
    public ResponseEntity<Object> getGenres(){

        List<GenreRes> genres = genreService.getGenres();

        return ResponseEntity.ok(genres);
    }

    @PostMapping("/genres")
    public ResponseEntity<Object> postGenre(@RequestBody @Valid GenreReq.PostReq req) {

        genreService.save(req);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/series")
    public ResponseEntity<Object> getSeries() {

        List<SeriesRes> seriesList = seriesService.getSeries();

        return ResponseEntity.ok(seriesList);
    }

    @PostMapping("/series")
    public ResponseEntity<Object> postSeries(@RequestBody @Valid SeriesReq.PostReq req) {

        seriesService.save(req);

        return ResponseEntity.noContent().build();
    }
}
