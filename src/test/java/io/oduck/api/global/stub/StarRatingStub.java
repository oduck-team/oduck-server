package io.oduck.api.global.stub;

import io.oduck.api.domain.starRating.entity.StarRating;
import java.util.ArrayList;
import java.util.List;

public class StarRatingStub {
    List<StarRating> starRatings = new ArrayList<>();

    public StarRatingStub() {
        StarRating starRating1 = StarRating.builder()
            .score(1)
            .build();

        StarRating starRating2 = StarRating.builder()
            .score(2)
            .build();

        StarRating starRating3 = StarRating.builder()
            .score(2)
            .build();

        StarRating starRating4 = StarRating.builder()
            .score(3)
            .build();

        starRatings.addAll(List.of(starRating1, starRating2, starRating3, starRating4));
    }

    public List<StarRating> getStarRatings() {
        return starRatings;
    }
}
