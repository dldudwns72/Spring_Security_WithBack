package me.yjlee.demospringsecurityform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// SpringWebSecurity 설정
@Configuration
@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE -10) // 실행 순서 결정
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 요청에 대한 인가
        http.authorizeRequests()
                .mvcMatchers("/", "/info","/account/**").permitAll() // 권한 상관없이 실행, /** 하면 뒤에 내용 다 허용
                .mvcMatchers("/admin").hasRole("ADMIN") // ADMIN 이라는 역할을 가질때만 실행
                .anyRequest().authenticated() // 기타 나머지에 대한 요청은 인증만 하면 접근 가능하다
                .and()
                .formLogin()
                .and()
                .httpBasic();

//        and()를 통하지 않고 따로 선언 해주어도 된다.
//        http.formLogin(); // login, loginout 기능
//        http.httpBasic(); // http 통신 지원
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
