package io.oduck.api.domain.anime.dto;

import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnimeReq {
  private String title;
  private String thumbnail;
  private String summary;
  private BroadcastType broadcastType;
  private int eposideCount;
  private int year;
  private Quarter quarter;
  private Rating rating;
  private Status status;
}
