package com.project.cookEats;

import com.project.cookEats.member.OAuth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2MemberService oAuth2MemberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );//.requestMatchers()에 URL 기재 가능 , /**는 모든 이라는 뜻이다.
        http.formLogin((formLogin) -> formLogin.loginPage("/member/login")
                .defaultSuccessUrl("/")
                .failureUrl("/member/login?result=false")
        );
        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/member/login")
                .defaultSuccessUrl("/")
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2MemberService)
                )
        );

        http.logout( logout -> logout.logoutUrl("/member/logout").logoutSuccessUrl("/") );

        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}