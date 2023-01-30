package com.content_i_like.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String memberEmail;

    //토큰이 없거나 bearer로 시작하는 토큰이 아닐 때 통과
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    try {
      memberEmail = jwtService.extractUserName(jwt);// todo extract the userEmail from JWT token;

      UserDetails userDetails = this.userDetailsService.loadUserByUsername(memberEmail);
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
      );
      authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
      );
      SecurityContextHolder.getContext().setAuthentication(authToken);


    } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
      throw new JwtException("유효하지 않은 토큰");
    } catch (ExpiredJwtException e) {
      throw new JwtException("토큰 기한 만료");
    } catch (SignatureException e) {
      throw new JwtException("사용자 인증 실패");
    } catch (SecurityException | PrematureJwtException e) {
      throw new JwtException("권한 없음. 접근 불가");
    } catch (Exception e) {
      log.error("JwtFilter 오류 발생");
      log.error("token: {}", jwt);
      log.error("Exception Message: {}", e.getMessage());
      e.printStackTrace();
      throw new JwtException("UNKNOWN");
    }

    filterChain.doFilter(request, response);

  }
}
