package me.yjlee.demospringsecurityform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE -15) // 실행 우선 순서 결정
public class AnotherSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected  void  configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .anyRequest().authenticated(); // 인증 되야 사용 가능
        http.formLogin();
        http.httpBasic();
    }
}
