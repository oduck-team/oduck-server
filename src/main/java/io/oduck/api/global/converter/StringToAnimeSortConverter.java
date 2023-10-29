package io.oduck.api.global.converter;

import org.springframework.core.convert.converter.Converter;

import static io.oduck.api.domain.anime.dto.AnimeReq.Sort;

public class StringToAnimeSortConverter implements Converter<String, Sort> {
    @Override
    public Sort convert(String sort) {
        return Sort.valueOf(sort.toUpperCase());
    }
}
