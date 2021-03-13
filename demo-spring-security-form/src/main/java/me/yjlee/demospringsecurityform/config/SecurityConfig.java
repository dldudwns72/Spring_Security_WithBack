package me.yjlee.demospringsecurityform.config;

import me.yjlee.demospringsecurityform.common.LoggingFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        http.addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class); // 성능 측정시 사용됨

        // 요청에 대한 인가
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**","/signup").permitAll() // 권한 상관없이 실행, /** 하면 뒤에 내용 다 허용
                .mvcMatchers("/admin").hasRole("ADMIN") // ADMIN 이라는 역할을 가질때만 실행
                .mvcMatchers("/user").hasRole("USER") // USER 이라는 역할을 가질때만 실행
                .anyRequest().authenticated() // 기타 나머지에 대한 요청은 인증만 하면 접근 가능하다
                //.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 이 방법으로 static 자원에 대한 인증 필터 제외를 설정 할 수있지만 아래 WebSecurity로 설정 권유
                .expressionHandler(expressionHandler());

//        and()를 통하지 않고 따로 선언 해주어도 된다.
        http.formLogin()
//                .usernameParameter("my-username")
//                .passwordParameter("my-password")
                .loginPage("/login")
                .permitAll()
        ; // login, loginout 기능

        http.httpBasic(); // http 통신 지원

        // Session 과 같은 기능으로 사용자의 정보가 Token 형식으로 들어가 있다, 사용자 정보를 저장 시 체크 여부 확인 할 수 있을듯
//        http.rememberMe()
//                .userDetailsService(accountService)
//                .key("rememver");

        http.sessionManagement()
                .sessionFixation().changeSessionId()
                .maximumSessions(1) // 세션 최대 개수 설정
                .maxSessionsPreventsLogin(false) // 추가 로그인 설정
                .expiredUrl("/login") // 세션 만료 시 이동할 URL
//                .invalidSessionUrl("/error") // 유효하지 않은 세션일 경우 보낼 URL

        ;

//        http.csrf().disable(); // csrf를 사용하지 않겠다 -> csrf 토큰을 발행하지 않음
        http.logout()
                .logoutUrl("/logout") // logout URL을 변경, default logout URL을 바꿀 수 있다.
                .logoutSuccessUrl("/") // logout 이후 다른 페이지로 redirect
                //.addLogoutHandler() // logout후 부가적인 handler 처리
        ;

        http.exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        // 따로 클래스를 구현해서 설정해도 된다
                        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        String username = principal.getUsername();
                        System.out.println(username + "is denied to access " + httpServletRequest.getRequestURI());
                        httpServletResponse.sendRedirect("/access-denied");
                    }
                })
                //.accessDeniedPage("/access-denied")
        ;

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
