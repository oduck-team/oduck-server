package io.oduck.api.global.config;

import io.oduck.api.global.converter.*;
import io.oduck.api.global.security.resolver.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // config에 converter 등록
        registry.addConverter(new StringToOrderDirectionConverter());
        registry.addConverter(new StringToBookmarkSortConverter());
        registry.addConverter(new StringToShortReviewConverter());
        registry.addConverter(new StringToAnimeSortConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }
}