package com.content_i_like.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "해당 페이지를 찾을 수 없습니다."),
  DUPLICATED_MEMBER_NAME(HttpStatus.CONFLICT, "이미 존재하고 있는 사용자입니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
  INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
  INCONSISTENT_INFORMATION(HttpStatus.CONFLICT, "일치하지 않는 정보입니다."),
  REJECT_PASSWORD(HttpStatus.CONFLICT, "비밀번호는 8~16자입니다."),
  UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "알 수 없는 에러가 발생했습니다."),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
  NOT_EXIST_INQUIRY_TITLE(HttpStatus.BAD_REQUEST, "제목이 없습니다."),
  NOT_EXIST_INQUIRY_CONTENT(HttpStatus.BAD_REQUEST, "내용이 없습니다."),

  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
  INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
  NOT_EXIST_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.")

  ;
  private HttpStatus status;
  private String message;
}
