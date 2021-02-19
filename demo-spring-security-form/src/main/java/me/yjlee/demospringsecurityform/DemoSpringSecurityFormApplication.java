package me.yjlee.demospringsecurityform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoSpringSecurityFormApplication {

	@Bean
	public PasswordEncoder passwordEncoder(){
		// 5버전 이후 Deprecated, 인코딩 방식 지정
//		return NoOpPasswordEncoder.getInstance();
		// 5버전 이후 권장, 다양한 인코딩 지원
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public static void main(String[] args) {

		SpringApplication.run(DemoSpringSecurityFormApplication.class, args);
	}

}
