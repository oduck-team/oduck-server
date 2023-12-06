package io.oduck.api.global.converter;

import io.oduck.api.domain.review.dto.ShortReviewReqDto.SortForProfile;
import org.springframework.core.convert.converter.Converter;

public class StringToMemerReviewsConverter implements Converter<String, SortForProfile> {

    @Override
    public SortForProfile convert(String sort) {
        return SortForProfile.valueOf(sort.toUpperCase());
    }
}
