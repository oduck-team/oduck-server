package io.oduck.api.global.converter;


import static io.oduck.api.domain.admin.dto.AdminReq.Sort;

import org.springframework.core.convert.converter.Converter;

public class StringToAdminAnimeSortConverter implements Converter<String, Sort> {
    @Override
    public Sort convert(String sort) {
        return Sort.valueOf(sort.toUpperCase());
    }
}
