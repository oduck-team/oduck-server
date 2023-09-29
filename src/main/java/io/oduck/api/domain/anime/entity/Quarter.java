package io.oduck.api.domain.anime.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Quarter {
  Q1(1),
  Q2(2),
  Q3(3),
  Q4(4);

  private final int value;
}
