package com.content_i_like.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 페이지를 찾을 수 없습니다.");

    private HttpStatus status;
    private String message;
}
