package me.yjlee.demospringsecurityform.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;
import java.util.List;

// SpringWebSecurity 설정
@Configuration
@EnableWebSecurity
//@Order(Ordered.LOWEST_PRECEDENCE -10) // 실행 순서 결정
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 권한(Role) 설정  admin 권한은 user 권한까지 다 가지고 있다.
    public SecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
//        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
//        webExpressionVoter.setExpressionHandler(handler);
//        List<AccessDecisionVoter<?extends Object>> voters = Arrays.asList();
//
//        return new AffirmativeBased(voters);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 요청에 대한 인가
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**").permitAll() // 권한 상관없이 실행, /** 하면 뒤에 내용 다 허용
                .mvcMatchers("/admin").hasRole("ADMIN") // ADMIN 이라는 역할을 가질때만 실행
                .mvcMatchers("/user").hasRole("USER") // USER 이라는 역할을 가질때만 실행
                .anyRequest().authenticated() // 기타 나머지에 대한 요청은 인증만 하면 접근 가능하다
                //.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 이 방법으로 static 자원에 대한 인증 필터 제외를 설정 할 수있지만 아래 WebSecurity로 설정 권유
                .expressionHandler(expressionHandler());

//        and()를 통하지 않고 따로 선언 해주어도 된다.
        http.formLogin(); // login, loginout 기능
        http.httpBasic(); // http 통신 지원

        http.csrf().disable(); // csrf를 사용하지 않겠다 -> csrf 토큰을 발행하지 않음

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL); // 현재 Thread에서 하위 Thread에게 SecurityContext 공유
    }

    @Override
    public void configure(WebSecurity web)  throws Exception{
        // 인증을 거치지 않고 사용 가능하도록 설정
        //web.ignoring().mvcMatchers("/favicon.ico");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // application.properties를 통하여 user 이름, 비밀번호, 권한 을 설정할 수 있다. But 유저 정보를 하나만 가질 수 있고 소스에 정보를 담고있어서 안좋음
    // PW 설정 : {noop} : 암호화를 하지 않음 , {암호화(인코딩) 방식}, In Memory 방식
    // User 설정
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("YJLee").password("{noop}123").roles("USER").and()
//                .withUser("admin").password("{noop}!@#").roles("ADMIN");
//    }
}
