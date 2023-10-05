package io.oduck.api.global.security.config;


import static org.springframework.security.config.Customizer.withDefaults;

import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.global.security.handler.ForbiddenHandler;
import io.oduck.api.global.security.handler.LoginFailureHandler;
import io.oduck.api.global.security.handler.LoginSuccessHandler;
import io.oduck.api.global.security.auth.service.CustomOAuth2UserService;
import io.oduck.api.global.security.handler.LogoutHandler;
import io.oduck.api.global.security.handler.UnauthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final LogoutHandler logoutHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final ForbiddenHandler forbiddenHandler;
    private final UnauthorizedHandler unauthorizedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 헤더 보안 설정
        http
            .headers((headers) -> headers
                .contentTypeOptions(withDefaults())
                .xssProtection(withDefaults())
                .cacheControl(withDefaults())
                .httpStrictTransportSecurity(withDefaults())
                .frameOptions(withDefaults())
            );

        // csrf 비활성, cors 기본 설정
        http
            .csrf(
                AbstractHttpConfigurer::disable
            )
            .cors(withDefaults());

        // form 로그인 비활성, httpBasic 비활성
        http
            .formLogin(
                AbstractHttpConfigurer::disable
            )
            .httpBasic(
                AbstractHttpConfigurer::disable
            );

        // 세션 설정
        http
            .sessionManagement(
                (sessionManagement) -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        // 인가 설정
        http
            .authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                    .requestMatchers("/auth/status").hasAnyAuthority(Role.MEMBER.name(), Role.ADMIN.name())
//                    .requestMatchers("docs/index.html").hasAuthority(Role.ADMIN.name())
                    .requestMatchers("oduckdmin/*").hasAuthority(Role.ADMIN.name())
                    .anyRequest().permitAll()
            );

        // 로그아웃 설정
        http
            .logout((logout) -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutHandler)
                .deleteCookies("oDuckio.sid")
                .invalidateHttpSession(true)
                .permitAll(false)
            );

        // OAuth2 로그인 설정
        http
            .oauth2Login(
                (oauth2Login) -> oauth2Login
                    .userInfoEndpoint((userInfoEndpoint) -> userInfoEndpoint
                        .userService(customOAuth2UserService)
                    )
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
            )
            .exceptionHandling(
                (exceptionHandling) -> exceptionHandling
                    .authenticationEntryPoint(unauthorizedHandler)
                    .accessDeniedHandler(forbiddenHandler)
            );

        return http.build();
    }
}