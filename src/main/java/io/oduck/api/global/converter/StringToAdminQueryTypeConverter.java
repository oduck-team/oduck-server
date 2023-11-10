package io.oduck.api.global.converter;

import io.oduck.api.domain.admin.dto.AdminReq.QueryType;
import org.springframework.core.convert.converter.Converter;

public class StringToAdminQueryTypeConverter implements Converter<String, QueryType> {
    @Override
    public QueryType convert(String type) {
        return QueryType.valueOf(type.toUpperCase());
    }
}
