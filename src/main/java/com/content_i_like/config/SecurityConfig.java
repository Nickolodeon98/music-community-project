package com.content_i_like.config;

import com.content_i_like.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;
  private final AuthenticationProvider authenticationProvider;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  private final String[] AUTHORIZATION = {"/api/v1/**", "/**", "http://www.thymeleaf.org/**", "http://www.ultraq.net.nz/**"};
  private final String[] TEST_URL = {"/api/v1/hello/**", "/api/v1/member/join",
      "/api/v1/member/login", "/api/v1/member/passwd/find_pw"
      , "/api/v1/test/**", "/upload", "/member/**", "/inquiry/**", "recommends/**", "/follow/**", "/search/tracks/**"};

  private final String[] SWAGGER = {"/v3/api-docs/**", "/swagger-ui/**"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers(TEST_URL)
        .permitAll()
        .requestMatchers(SWAGGER)
        .permitAll()
        .requestMatchers(HttpMethod.GET, AUTHORIZATION)
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .and()
        .oauth2Login()                  // OAuth2 로그인 설정
        //.loginPage("/api/v1/oauth/google")
        //.defaultSuccessUrl("/api/v1/oauth/naver")
        //.defaultSuccessUrl("/api/v1/oauth/google")
        .defaultSuccessUrl("/")
        .failureUrl("/")
        .userInfoEndpoint()             // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정
        .userService(customOAuth2UserService);     // OAuth2 로그인 성공 시, 후작업을 진행할 UserService 인터페이스 구현체 등록;
    return http.build();
  }
}