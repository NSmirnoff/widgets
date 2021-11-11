package org.widget.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RequiredEntityNotFoundException.class)
    protected ResponseEntity<Object> handleRequiredEntityNotFound(
            RequiredEntityNotFoundException e, WebRequest request
    ) {
        Map<String, String> body = Map.of("message", e.getMessage());
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleAlconException(BadRequestException e, WebRequest request) {
        Map<String, String> body = Map.of("message", e.getMessage());
        return handleExceptionInternal(e, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @NonNull
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {

        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(this::buildBody)
                .collect(Collectors.toList());

        var response = new HashMap<>() {{
            put("message", "Invalid request");
            put("details", errors);
        }};

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private Map<String, Object> buildBody(FieldError f) {
        Map<String, Object> map  = new HashMap<>() {{
            put("title", "Invalid '" + f.getObjectName() + "' property");
        }};

        HashMap<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("pointer", f.getField());
        if (f.getRejectedValue() != null) {
            sourceMap.put("parameter", f.getRejectedValue().toString());
        }

        map.put("source", sourceMap);
        return map;
    }
}
