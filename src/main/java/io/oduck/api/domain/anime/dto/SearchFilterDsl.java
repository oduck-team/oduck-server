package io.oduck.api.domain.anime.dto;

import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static io.oduck.api.domain.anime.dto.AnimeReq.*;

@Getter
@AllArgsConstructor
public class SearchFilterDsl {
    List<Long> genreIds;
    List<BroadcastType> broadcastTypes;
    List<EpisodeCountEnum> episodeCountEnums;
    List<Integer> years;
    List<Quarter> quarters;
}
