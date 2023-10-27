package io.oduck.api.global.security.auth.service;

import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.member.repository.MemberProfileRepository;
import io.oduck.api.global.exception.UnauthorizedException;
import io.oduck.api.global.security.auth.dto.AuthResDto.Status;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LocalAuthDto;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import io.oduck.api.global.security.auth.repository.AuthLocalRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthLocalRepository authLocalRepository;
    private final AuthenticationManager authenticationManager;
    private final MemberProfileRepository memberProfileRepository;
    private final HttpSession httpSession;

    public Status getStatus(AuthUser user) {
        MemberProfile memberProfile = memberProfileRepository.findByMemberId(user.getId())
            .orElseThrow(() -> new UnauthorizedException("UnAuthorized"));

        return Status.builder()
            .memberId(user.getId())
            .role(user.getRole())
            .name(memberProfile.getName())
            .description(memberProfile.getInfo())
            .thumbnail(memberProfile.getThumbnail())
            .point(memberProfile.getPoint())
            .build();
    }

    public AuthLocal getAuthLocal(String email) {
        return authLocalRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("UnAuthorized"));
    }

    public void login(LocalAuthDto localAuthDto) {
        String username = localAuthDto.getEmail();

        AuthLocal authLocal = getAuthLocal(username);
        Role role = authLocal.getMember().getRole();

        String password = extractPasswordIfAdmin(role, localAuthDto.getPassword());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());
        httpSession.setAttribute("user", new AuthUser(authLocal.getMember().getId(), LoginType.LOCAL));
    }

    private String extractPasswordIfAdmin(Role role, String password) {
        if (role.equals(Role.MEMBER)) return password;

        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

        String currentTime = password.substring(0, 4);
        if (!now.format(formatter).equals(currentTime)) {
            throw new UnauthorizedException("Unauthorized");
        }

        return password.substring(4);
    }
}
