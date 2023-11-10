package io.oduck.api.global.converter;

import io.oduck.api.domain.anime.entity.Status;
import org.springframework.core.convert.converter.Converter;

public class StringToStatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String type) {
        return Status.valueOf(type.toUpperCase());
    }
}
