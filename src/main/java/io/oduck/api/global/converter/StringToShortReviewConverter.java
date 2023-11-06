package io.oduck.api.global.converter;


import io.oduck.api.domain.review.dto.ShortReviewReqDto.Sort;
import org.springframework.core.convert.converter.Converter;

public class StringToShortReviewConverter implements Converter<String, Sort> {


    @Override
    public Sort convert(String sort) {
        return Sort.valueOf(sort.toUpperCase());
    }
}
