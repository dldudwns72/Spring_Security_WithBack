package me.yjlee.demospringsecurityform.account;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "YoungJun",roles = "USER")
public @interface WithUser {
    // USER의 어노테이션 화
}
