package io.oduck.api.domain.admin.controller;

import static io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PostReq;

import io.oduck.api.domain.anime.dto.AnimeReq.PatchGenreIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchOriginalAuthorIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchSeriesIdReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchStudioIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchVoiceActorIdsReq;
import io.oduck.api.domain.anime.entity.AnimeGenre;
import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import io.oduck.api.domain.anime.entity.AnimeStudio;
import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import io.oduck.api.domain.anime.service.AnimeService;
import io.oduck.api.domain.series.entity.Series;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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
        // TODO: 애니 추가 로직 구현
        animeService.save(req);

        return ResponseEntity.ok().build();
    }

    //애니 관련 수정
    @PatchMapping("/animes/{animeId}")
    public ResponseEntity<Object> patchAnime(
        @PathVariable Long animeId, @RequestBody @Valid PatchAnimeReq req
    ){
        //TODO: 애니 수정 로직 구현
        animeService.update(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니의 원작 작가 수정
    @PatchMapping("/animes/{animeId}/original-authors")
    public ResponseEntity<Object> patchAnimeOriginalAuthors(
        @PathVariable Long animeId, @RequestBody PatchOriginalAuthorIdsReq req
    ){
        List<Long> originalAuthorIds = req.getOriginalAuthorIds();
        //TODO: 컨트롤러에서 애니 아이디와 원작 작가 아이디로 AnimeOriginalAuthor들을 찾아야 함.
        List<AnimeOriginalAuthor> animeOriginalAuthors = new ArrayList<>();

        //TODO: 애니 수정 로직 구현
        animeService.updateAnimeOriginalAuthors(animeId, animeOriginalAuthors);

        return ResponseEntity.noContent().build();
    }

    // 애니의 스튜디오 수정
    @PatchMapping("/animes/{animeId}/studios")
    public ResponseEntity<Object> patchAnimeStudios(
        @PathVariable Long animeId, @RequestBody PatchStudioIdsReq req
    ){
        List<Long> originalAuthorIds = req.getStudioIds();
        //TODO: 컨트롤러에서 애니 아이디와 원작 작가 아이디로 AnimeOriginalAuthor들을 찾아야 함.
        List<AnimeStudio> animeStudios = new ArrayList<>();

        //TODO: 애니 수정 로직 구현
        animeService.updateAnimeStudios(animeId, animeStudios);

        return ResponseEntity.noContent().build();
    }
    // 애니의 성우 수정
    @PatchMapping("/animes/{animeId}/voice-actors")
    public ResponseEntity<Object> patchAnimeVoiceActors(
        @PathVariable Long animeId, @RequestBody PatchVoiceActorIdsReq req
    ){
        List<Long> voiceActorIds = req.getVoiceActorIds();
        //TODO: 컨트롤러에서 애니 아이디와 성우 아이디로 AnimeOriginalAuthor들을 찾아야 함.
        List<AnimeVoiceActor> animeVoiceActors = new ArrayList<>();

        //TODO: 애니 수정 로직 구현
        animeService.updateAnimeVoiceActors(animeId, animeVoiceActors);

        return ResponseEntity.noContent().build();
    }

    // 애니의 장르 수정
    @PatchMapping("/animes/{animeId}/genres")
    public ResponseEntity<Object> patchAnimeGenres(
        @PathVariable Long animeId, @RequestBody PatchGenreIdsReq req
    ){
        List<Long> genreIds = req.getGenreIds();
        //TODO: 컨트롤러에서 애니 아이디와 장르 아이디로 AnimeGenre들을 찾아야 함.
        List<AnimeGenre> animeGenres = new ArrayList<>();

        //TODO: 애니 수정 로직 구현
        animeService.updateAnimeGenres(animeId, animeGenres);

        return ResponseEntity.noContent().build();
    }

    // 애니의 시리즈 수정
    @PatchMapping("/animes/{animeId}/series")
    public ResponseEntity<Object> patchAnimeSeries(
        @PathVariable Long animeId, @RequestBody PatchSeriesIdReq req
    ){
        Long seriesId = req.getSeriesId();
        //TODO: 컨트롤러에서 시리즈 아이디로 시리즈를 찾아야 함.
        Series series = null;

        //TODO: 애니 수정 로직 구현
        animeService.update(animeId, series);

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
