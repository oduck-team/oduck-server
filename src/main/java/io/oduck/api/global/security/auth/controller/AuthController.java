package io.oduck.api.global.security.auth.controller;

import io.oduck.api.global.common.SingleResponse;
import io.oduck.api.global.security.auth.dto.AuthResDto.Status;
import io.oduck.api.global.security.auth.dto.LocalAuthDto;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import io.oduck.api.global.security.auth.service.AuthService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody LocalAuthDto localAuthDto
    ) {

        authService.login(localAuthDto);
        return ResponseEntity.status(302).location(URI.create("http://localhost:5173")).build();
    }

    @GetMapping("/status")
    public ResponseEntity<Status> status(
        @LoginUser AuthUser user
    ) {

        Status res = authService.getStatus(user.getId());

        return ResponseEntity.ok(res);
    }
}
