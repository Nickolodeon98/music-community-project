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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtExceptionFilter implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    String exception = String.valueOf(request.getAttribute("exception"));
    ErrorCode errorCode;

    if (exception == null) {
      setErrorResponse(response, ErrorCode.NOT_EXIST_TOKEN);
    }
    if (exception.equals(ErrorCode.INVALID_TOKEN.name())) {
      setErrorResponse(response, ErrorCode.INVALID_TOKEN);
    }
    if (exception.equals(ErrorCode.INVALID_PERMISSION.name())) {
      setErrorResponse(response, ErrorCode.INVALID_PERMISSION);
    }
    if (exception.equals(ErrorCode.EXPIRED_TOKEN.name())) {
      setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
    }
    if (exception.equals(ErrorCode.UNKNOWN_ERROR.name())) {
      setErrorResponse(response, ErrorCode.UNKNOWN_ERROR);
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
