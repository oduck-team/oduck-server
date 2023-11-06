package io.oduck.api.domain.anime.dto;

import static io.oduck.api.domain.anime.dto.AnimeReq.EpisodeCountEnum;

import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class SearchFilterDsl {
    List<Long> genreIds;
    List<BroadcastType> broadcastTypes;
    List<EpisodeCountEnum> episodeCountEnums;
    List<Integer> years;
    List<Quarter> quarters;

    public SearchFilterDsl(List<Long> genreIds, List<BroadcastType> broadcastTypes,
        List<EpisodeCountEnum> episodeCountEnums, List<Integer> years, List<Quarter> quarters) {
        this.genreIds = genreIds;
        this.broadcastTypes = broadcastTypes;
        this.episodeCountEnums = episodeCountEnums;

        int currentYear = LocalDate.now().getYear();
        this.years = years.stream()
            .filter(year -> year != currentYear)
            .collect(Collectors.toList());

        this.quarters = quarters;
    }
}
