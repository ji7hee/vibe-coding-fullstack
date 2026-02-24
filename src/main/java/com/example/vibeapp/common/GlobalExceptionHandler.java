package com.example.vibeapp.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 전역 예외 처리기 — 모든 @RestController에서 발생하는 예외를 JSON으로 반환
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404: 게시글을 찾을 수 없는 경우 */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.status(404).body(Map.of(
                "status", 404,
                "message", e.getMessage()
        ));
    }

    /** 400: @Valid 검증 실패 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("입력값이 올바르지 않습니다.");

        return ResponseEntity.status(400).body(Map.of(
                "status", 400,
                "message", message
        ));
    }

    /** 404: 정적 리소스 또는 URL을 찾을 수 없음 */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResource(NoResourceFoundException e) {
        return ResponseEntity.status(404).body(Map.of(
                "status", 404,
                "message", "요청한 리소스를 찾을 수 없습니다."
        ));
    }

    /** 500: 그 외 서버 오류 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        return ResponseEntity.status(500).body(Map.of(
                "status", 500,
                "message", "서버 오류가 발생했습니다."
        ));
    }
}
