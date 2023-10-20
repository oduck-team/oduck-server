package io.oduck.api.global.converter;

import org.springframework.core.convert.converter.Converter;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.Sort;

public class StringToBookmarkSortConverter implements Converter<String, Sort> {

    @Override
    public Sort convert(String sort) {
        return Sort.valueOf(sort.toUpperCase());
    }
}