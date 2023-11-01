package io.oduck.api.global.security.resolver;

import io.oduck.api.global.security.auth.dto.CustomOAuth2User;
import io.oduck.api.global.security.auth.dto.CustomUserDetails;
import io.oduck.api.global.security.auth.dto.LoginUser;
import io.oduck.api.global.security.auth.dto.AuthUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = AuthUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
//        Object user = httpSession.getAttribute("user");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == "anonymousUser" || principal == null) {
            return null;
        }

        AuthUser user;

        if (principal.getClass().getSimpleName().equals("CustomUserDetails")) {
            user = getAuthUserFromCustomUserDetails(principal);
        } else {
            user = getAuthUserFromOAuth2User(principal);
        }

        return  user;
    }

    private AuthUser getAuthUserFromOAuth2User(Object principal) {
        return AuthUser.builder()
            .id(
                ((CustomOAuth2User)principal).getId()
            )
            .role(
                ((CustomOAuth2User)principal).getAuthorities().stream().findFirst().get().getAuthority().toUpperCase())
            .loginType(
                ((CustomOAuth2User)principal).getLoginType()
            )
            .build();
    }

    private AuthUser getAuthUserFromCustomUserDetails(Object principal) {
        return AuthUser.builder()
            .id(
                ((CustomUserDetails)principal).getId()
            )
            .role(
                ((CustomUserDetails)principal).getAuthorities().stream().findFirst().get().getAuthority().toUpperCase()
            )
            .loginType(
                ((CustomUserDetails)principal).getLoginType()
            )
            .build();
    }
}