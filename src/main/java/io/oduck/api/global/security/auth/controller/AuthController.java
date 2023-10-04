package io.oduck.api.global.security.auth.controller;

import io.oduck.api.global.exception.UnauthorizedException;
import io.oduck.api.global.security.auth.dto.SessionUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/status")
    public String status(
        @LoginUser SessionUser user
    ) {

        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        return "success";
    }
}
