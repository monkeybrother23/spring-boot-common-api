package com.albert.common.web.handler;


import com.albert.common.web.exception.ApiException;
import com.albert.common.web.result.ApiResult;
import com.albert.common.web.result.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.StringJoiner;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     * ApiException
     */
    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResult<String> apiExceptionHandler(ApiException e) {
        return ApiResult.fail(e);
    }

    /**
     * 参数绑定
     * BindException
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
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
     * Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<String> exceptionHandler(Exception e) {
        return ApiResult.fail(e.getMessage(), ApiStatus.ERROR);
    }

}
