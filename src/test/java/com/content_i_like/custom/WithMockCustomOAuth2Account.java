package com.content_i_like.custom;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2AccountSecurityContextFactory.class)
public @interface WithMockCustomOAuth2Account {
    String username() default "username";
    String email() default "email";
    String name() default "name";
    String role() default "USER";

    String registrationId() default "ID";

}
