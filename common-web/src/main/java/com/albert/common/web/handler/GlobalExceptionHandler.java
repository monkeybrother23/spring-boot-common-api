package com.albert.common.web.handler;


import com.albert.common.web.exception.ApiException;
import com.albert.common.web.result.ApiResult;
import com.albert.common.web.result.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.StringJoiner;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     * ApiException
     */
    @ExceptionHandler(value = ApiException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResult<String> apiExceptionHandler(ApiException e) {
        return ApiResult.fail(e);
    }

    /**
     * 参数绑定
     * BindException
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResult<String> bindExceptionHandler(BindException e) {
        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(temp -> stringJoiner.add(temp.getField() + ":" + temp.getDefaultMessage()));
        String msg = stringJoiner.toString();
        logger.warn("bindExceptionHandler:{}", msg);
        return ApiResult.fail(msg, ApiStatus.ERROR);
    }

    /**
     * AccessDeniedException
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ApiResult<String> accessExceptionHandler(AccessDeniedException e) {
        return ApiResult.fail(e.getMessage(), ApiStatus.FORBIDDEN);
    }

    /**
     * Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<String> exceptionHandler(Exception e) {
        return ApiResult.fail(e.getMessage(), ApiStatus.ERROR);
    }

}
