package io.oduck.api.global.MockMember;

import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.global.security.auth.dto.CustomUserDetails;
import io.oduck.api.global.security.auth.dto.AuthUser;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WithMemberDetailsSecurityContextFactory implements
    WithSecurityContextFactory<WithCustomMockMember> {
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();
    protected MockHttpSession session;
    MockHttpServletRequest servletRequest;

    @Override
    public SecurityContext createSecurityContext(
        WithCustomMockMember customUser) {

        CustomUserDetails principal = new CustomUserDetails(customUser.id(), customUser.email(),
            customUser.password(), customUser.role());

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(principal,
            principal.getPassword(), principal.getAuthorities());
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);

        session = new MockHttpSession();
        AuthUser user = new AuthUser(1L, LoginType.LOCAL);
        servletRequest = new MockHttpServletRequest();
        servletRequest.setSession(session);

        session.setAttribute("user", user);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));
        return context;
    }
}