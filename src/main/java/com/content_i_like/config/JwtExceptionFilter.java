package com.content_i_like.config;

import com.content_i_like.domain.Response;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      System.out.println(e.getMessage());
      if (e.getMessage().equals("토큰 기한 만료")) {
        setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
      } else if (e.getMessage().equals("유효하지 않은 토큰") || e.getMessage().equals("사용자 인증 실패")) {
        setErrorResponse(response, ErrorCode.INVALID_TOKEN);
      } else if (e.getMessage().equals("권한 없음. 접근 불가")) {
        setErrorResponse(response, ErrorCode.INVALID_PERMISSION);
      } else {
        setErrorResponse(response, ErrorCode.UNKNOWN_ERROR);
      }
    }
  }

  private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode)
      throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(errorCode.getStatus().value());

    Map<String, Object> result = new HashMap<>();
    result.put("errorCode", errorCode.name());
    result.put("message", errorCode.getMessage());

    response.getWriter().println(
        objectMapper.writeValueAsString(
            Response.error("ERROR", result)));
  }

}
