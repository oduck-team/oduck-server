package io.oduck.api.domain.admin.controller;

import static io.oduck.api.domain.admin.dto.AdminReq.SearchFilter;

import io.oduck.api.domain.admin.dto.AdminReq;
import io.oduck.api.domain.admin.dto.AdminReq.QueryType;
import io.oduck.api.domain.admin.dto.AdminRes;
import io.oduck.api.domain.anime.dto.AnimeReq;
import io.oduck.api.domain.anime.service.AnimeService;
import io.oduck.api.domain.genre.dto.GenreReq;
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
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.PageResponse;
import io.oduck.api.global.exception.BadRequestException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // 관리자 애니 조회
    @GetMapping("/animes")
    public ResponseEntity<Object> getAnimes(
        @RequestParam(required = false) @Length(min = 0, max = 50) String query,
        QueryType queryType,
        @RequestParam(required = false, defaultValue = "latest") AdminReq.Sort sort,
        @RequestParam(required = false, defaultValue = "DESC") OrderDirection order,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(100) int size,
        SearchFilter searchFilter
    ){
        int validatedPage = validatePage(page);

        PageResponse<AdminRes.SearchResult> res = animeService.getPageByCondition(
            query,
            queryType,
            validatedPage,
            size,
            sort,
            order,
            searchFilter
        );

        return ResponseEntity.ok(res);
    }

    private void validateQueryLength(String query, int maxLength) {
        if(query != null) {
            if(query.length() > maxLength){
                throw new BadRequestException("글자수는 50자를 넘을 수 없습니다.");
            }
        }
    }

    private int validatePage(int page) {
        // 클라이언트는 페이지 번호를 1부터 시작, 서버에서는 0부터 시작
        if(page <= 1) {
            return 0;
        }else {
            return page - 1;
        }
    }

    //애니 관련 수정
    @PatchMapping("/animes/{animeId}")
    public ResponseEntity<Object> patchAnime(
        @PathVariable Long animeId, @RequestBody @Valid AnimeReq.PatchAnimeReq req
    ){

        animeService.update(animeId, req);

        return ResponseEntity.noContent().build();
    }

    // 애니 삭제
    @DeleteMapping("/animes/{animeId}")
    public ResponseEntity<Object> deleteAnime(@PathVariable Long animeId) {

        animeService.delete(animeId);

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

    // 원작 작가 추가
    @PostMapping("/original-authors")
    public ResponseEntity<Object> postOriginalAuthor(@RequestBody @Valid OriginalAuthorReq.PostReq req) {

        originalAuthorService.save(req);

        return ResponseEntity.noContent().build();
    }

    // 원작 작가 조회
    @GetMapping("/original-authors")
    public ResponseEntity<Object> getOriginalAuthors(){

        List<OriginalAuthorRes> originalAuthors = originalAuthorService.getOriginalAuthors();

        return ResponseEntity.ok(originalAuthors);
    }

    // 원작 작가 수정
    @PatchMapping("/original-authors/{originalAuthorId}")
    public ResponseEntity<Object> patchOriginalAuthor(
        @PathVariable Long originalAuthorId, @RequestBody @Valid OriginalAuthorReq.PatchReq req
    ){

        originalAuthorService.update(originalAuthorId, req.getName());

        return ResponseEntity.noContent().build();
    }

    // 원작 작가 삭제
    @DeleteMapping("/original-authors/{originalAuthorId}")
    public ResponseEntity<Object> deleteOriginalAuthor(
        @PathVariable Long originalAuthorId
    ){

        originalAuthorService.delete(originalAuthorId);

        return ResponseEntity.noContent().build();
    }

    // 성우 추가
    @PostMapping("/voice-actors")
    public ResponseEntity<Object> postVoiceActor(@RequestBody @Valid VoiceActorReq.PostReq req) {

        voiceActorService.save(req);

        return ResponseEntity.noContent().build();
    }

    // 성우 조회
    @GetMapping("/voice-actors")
    public ResponseEntity<Object> getVoiceActors() {

        List<VoiceActorRes> voiceActors = voiceActorService.getVoiceActors();

        return ResponseEntity.ok(voiceActors);
    }

    // 성우 수정
    @PatchMapping("/voice-actors/{voiceActorId}")
    public ResponseEntity<Object> patchVoiceActor(
        @PathVariable Long voiceActorId, @RequestBody @Valid VoiceActorReq.PatchReq req
    ){

        voiceActorService.update(voiceActorId, req.getName());

        return ResponseEntity.noContent().build();
    }

    // 성우 삭제
    @DeleteMapping("/voice-actors/{voiceActorId}")
    public ResponseEntity<Object> deleteVoiceActor(
        @PathVariable Long voiceActorId
    ){

        voiceActorService.delete(voiceActorId);

        return ResponseEntity.noContent().build();
    }

    // 스튜디오 추가
    @PostMapping("/studios")
    public ResponseEntity<Object> postStudio(@RequestBody @Valid StudioReq.PostReq req) {

        studioService.save(req);

        return ResponseEntity.noContent().build();
    }

    // 스튜디오 조회
    @GetMapping("/studios")
    public ResponseEntity<Object> getStudios() {

        List<StudioRes> studios = studioService.getStudios();

        return ResponseEntity.ok(studios);
    }

    // 스튜디오 수정
    @PatchMapping("/studios/{studioId}")
    public ResponseEntity<Object> patchStudio(
        @PathVariable Long studioId, @RequestBody @Valid StudioReq.PatchReq req
    ){

        studioService.update(studioId, req.getName());

        return ResponseEntity.noContent().build();
    }

    // 스튜디오 삭제
    @DeleteMapping("/studios/{studioId}")
    public ResponseEntity<Object> deleteStudio(
        @PathVariable Long studioId
    ){

        studioService.delete(studioId);

        return ResponseEntity.noContent().build();
    }

    // 장르 추가
    @PostMapping("/genres")
    public ResponseEntity<Object> postGenre(@RequestBody @Valid GenreReq.PostReq req) {

        genreService.save(req);

        return ResponseEntity.noContent().build();
    }

    // 장르 수정
    @PatchMapping("/genres/{genreId}")
    public ResponseEntity<Object> patchGenre(
        @PathVariable Long genreId, @RequestBody @Valid GenreReq.PatchReq req
    ){

        genreService.update(genreId, req.getName());

        return ResponseEntity.noContent().build();
    }

    // 장르 삭제
    @DeleteMapping("/genres/{genreId}")
    public ResponseEntity<Object> deleteGenre(
        @PathVariable Long genreId
    ){

        genreService.delete(genreId);

        return ResponseEntity.noContent().build();
    }

    // 시리즈 추가
    @PostMapping("/series")
    public ResponseEntity<Object> postSeries(@RequestBody @Valid SeriesReq.PostReq req) {

        seriesService.save(req);

        return ResponseEntity.noContent().build();
    }

    // 시리즈 조회
    @GetMapping("/series")
    public ResponseEntity<Object> getSeries() {

        List<SeriesRes> seriesList = seriesService.getSeries();

        return ResponseEntity.ok(seriesList);
    }

    // 시리즈 수정
    @PatchMapping("/series/{seriesId}")
    public ResponseEntity<Object> patchSeries(
        @PathVariable Long seriesId, @RequestBody @Valid SeriesReq.PatchReq req
    ){

        seriesService.update(seriesId, req.getTitle());

        return ResponseEntity.noContent().build();
    }

    // 시리즈 삭제
    @DeleteMapping("/series/{seriesId}")
    public ResponseEntity<Object> deleteSeries(
        @PathVariable Long seriesId
    ){

        seriesService.delete(seriesId);

        return ResponseEntity.noContent().build();
    }
}
