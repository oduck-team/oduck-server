package io.oduck.api.global.mockMember;

import io.oduck.api.domain.member.entity.Role;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMemberDetailsSecurityContextFactory.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface WithCustomMockMember {
    long id() default 1L;

    String email() default "bob@gmail.com";

    String password() default "Qwer!234";

    Role role() default Role.MEMBER;
}