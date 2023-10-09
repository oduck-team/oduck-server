package io.oduck.api.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.global.security.auth.dto.CustomUserDetails;
import io.oduck.api.global.security.auth.dto.LocalAuthDto;
import io.oduck.api.global.security.auth.dto.SessionUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LocalAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String CONTENT_TYPE = "application/json";
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        // objectMapper.readValue(request.getInputStream(), LoginDto.class)를 통해 ServletInputStream 을 LoginDto 클래스의 객체로 역직렬화(Deserialization)
        LocalAuthDto loginAuthDto = objectMapper.readValue(request.getInputStream(), LocalAuthDto.class);
        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken(loginAuthDto.getEmail(), loginAuthDto.getPassword());

        return this.authenticationManager.authenticate(authRequest);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        SessionUser sessionUser = new SessionUser(userDetails.getId(), LoginType.LOCAL);
        HttpSession session = request.getSession();
        session.setAttribute("user", sessionUser);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}