package io.oduck.api.domain.anime.controller;

import io.oduck.api.domain.anime.dto.AnimeRes;
import io.oduck.api.domain.anime.dto.AnimeRes.StarRatingAvg;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Status;
import io.oduck.api.domain.anime.service.AnimeService;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.exception.BadRequestException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.oduck.api.domain.anime.dto.AnimeReq.*;
import static io.oduck.api.domain.anime.dto.AnimeRes.DetailResult;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/animes")
@Slf4j
public class AnimeController {

    private final AnimeService animeService;

    // 애니 검색 조회
    @GetMapping
    public ResponseEntity<Object> getAnimesBySearchCondition(
            @RequestParam(required = false) @Length(min = 0, max = 50) String query,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "latest") Sort sort,
            @RequestParam(required = false, defaultValue = "DESC") OrderDirection order,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) int size,
            @ModelAttribute SearchFilter searchFilter
    ){

        List<Long> genreIds = searchFilter.getGenreIds();
        List<BroadcastType> broadcastTypes = searchFilter.getBroadcastTypes();
        List<EpisodeCountEnum> episodeCountEnums = searchFilter.getEpisodeCounts();

        List<Integer> years = searchFilter.getYears();
        extractCurrentYearsNotInCurrentYear(years);

        List<Quarter> quarters = searchFilter.getQuarters();
        validateDuplicateQuarters(quarters);

        List<Status> statuses = searchFilter.getStatuses();

        SearchFilterDsl searchFilterDsl = new SearchFilterDsl(genreIds, broadcastTypes, episodeCountEnums, years, quarters, statuses);

        SliceResponse<AnimeRes.SearchResult> res = animeService.getSliceByCondition(query, cursor,  sort,
            order, size, searchFilterDsl);
        return ResponseEntity.ok(res);
    }

    // 애니 아이디로 상세 조회
    @GetMapping("/{animeId}")
    public ResponseEntity<Object> getAnimeById(@PathVariable Long animeId){
        
        DetailResult res = animeService.getAnimeById(animeId);

        return ResponseEntity.ok(res);
    }

    private List<Integer> extractCurrentYearsNotInCurrentYear(List<Integer> years) {
        int currentYear = LocalDate.now().getYear();

        return years.stream()
                .filter(year -> year != currentYear)
                .collect(Collectors.toList());
    }

    private void validateDuplicateQuarters(List<Quarter> quarters) {
        if(quarters == null) {
            quarters = null;
        }else{
            Set<Quarter> quarterSet = new HashSet<>(quarters);
            if (quarterSet.size() != quarters.size()) {
                throw new BadRequestException("Quarters에 중복된 값이 포함되어 있습니다.");
            }
        }
    }
}
