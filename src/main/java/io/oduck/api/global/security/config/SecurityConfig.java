package io.oduck.api.global.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.oduck.api.domain.member.entity.Role;
//import io.oduck.api.global.security.filter.LocalAuthenticationFilter;
import io.oduck.api.global.security.handler.ForbiddenHandler;
import io.oduck.api.global.security.handler.LoginFailureHandler;
import io.oduck.api.global.security.handler.LoginSuccessHandler;
import io.oduck.api.global.security.auth.service.SocialLoginService;
import io.oduck.api.global.security.handler.LogoutHandler;
import io.oduck.api.global.security.handler.UnauthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

//    private final ObjectMapper objectMapper;
    private final LogoutHandler logoutHandler;
    private final SocialLoginService socialLoginService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final ForbiddenHandler forbiddenHandler;
    private final UnauthorizedHandler unauthorizedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 헤더 보안 설정
        http
            .headers((headers) ->
                headers
                    .contentTypeOptions(withDefaults())
                    .xssProtection(withDefaults())
                    .cacheControl(withDefaults())
                    .httpStrictTransportSecurity(withDefaults())
                    .frameOptions(withDefaults())
            );

        // csrf 비활성, cors 기본 설정
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults());

        // form 로그인 비활성, httpBasic 비활성
        http
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        // 세션 설정
        http
            .sessionManagement((sessionManagement) ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        // 인가 설정
        http
            .authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                    .requestMatchers("/auth/status").hasAnyAuthority(Role.MEMBER.name(), Role.ADMIN.name())
                    // .requestMatchers("docs/index.html").hasAuthority(Role.ADMIN.name())
                    .requestMatchers("oduckdmin/*").hasAuthority(Role.ADMIN.name())
                    .anyRequest().permitAll()
            );

        // 로그아웃 설정
        http
            .logout((logout) ->
                logout
                    .logoutUrl("/auth/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                    .logoutSuccessHandler(logoutHandler).deleteCookies("oDuckio.sid")
                    .invalidateHttpSession(true).permitAll(false)
            );

        // Custom Configurer : LocalAuthenticationFilter 를 등록하는 역할
//        http
//            .apply(new CustomFilterConfigurer());

        // OAuth2 로그인 설정
        http
            .oauth2Login((oauth2Login) ->
                oauth2Login
                .userInfoEndpoint((userInfoEndpoint) ->
                    userInfoEndpoint
                        .userService(socialLoginService)
                )
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
            );

        // 예외 처리 설정
        // 인증 인가 예외 처리
        http
            .exceptionHandling((exceptionHandling) ->
                exceptionHandling
                    .authenticationEntryPoint(unauthorizedHandler)
                    .accessDeniedHandler(forbiddenHandler)
            );

        return http.build();
    }

    // PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Spring Security 에서 지원하는 PasswordEncoder 구현 객체를 생성해주는 컴포넌트.
        // DelegatingPasswordEncoder를 통해 애플리케이션에서 사용할 PasswordEncoder 를 결정하고,
        // 결정된 PasswordEncoder로 사용자가 입력한 패스워드를 단방향으로 암호화 해줌.
        // 기본 BCrypt
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    // Custom Configurer : CustomFilterConfigurer 는 직접 구현한 필터인 JwtAuthenticationFilter 를 등록하는 역할
//    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        // AbstractHttpConfigurer 를 상속하여 구현
        // AbstractHttpConfigurer<AbstractHttpConfigurer 를 상속하는 타입, HttpSecurityBuilder 를 상속하는 타입> 을 지정

        // configure() 메서드를 오버라이드해서 Configuration 을 커스터마이징
//        @Override
//        public void configure(HttpSecurity builder) throws Exception {
//
//            // getSharedObject() 를 통해서 Spring Security 의 설정을 구성하는 SecurityConfigurer 간에 공유되는 객체를 획득가능
//            // getSharedObject(AuthenticationManager.class)를 통해 AuthenticationManager 객체 획득
//            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
//
//            // LocalAuthenticationFilter 생성
//            LocalAuthenticationFilter localAuthenticationFilter = new LocalAuthenticationFilter(authenticationManager, objectMapper);
//
//            // url과 httpMethod 를 통해 요청을 필터링하는데 사용할 RequestMatcher 를 지정
//            localAuthenticationFilter.setRequiresAuthenticationRequestMatcher(
//                new AntPathRequestMatcher("/auth/login", "POST")
//            );
//
//            // 인증에 성공하면 SecurityContext 에 Authentication 객체를 저장하고 로드하는데,
//            // 이러한 역할을 SecurityContextRepository 클래스에서 담당.
//            SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
//            localAuthenticationFilter.setSecurityContextRepository(contextRepository);
//
//            // 추가 처리 Custom Handler 를 필터에 등록
//            localAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
//            localAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);
//
//            // Spring Security Filter Chain에 추가
//            builder
//                .addFilter(localAuthenticationFilter);
//        }
//    }
}
