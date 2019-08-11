package cn.net.metadata.base.config.advice;

import cn.net.metadata.base.common.RestResult;
import cn.net.metadata.base.common.RestResultGenerator;
import cn.net.metadata.base.utility.Message;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config
 * @date 2018/7/19 17:27
 */
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    /**
     * 统一处理验证失败的提示信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return RestResultGenerator
            .error("数据校验失败：" + e.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage()).collect(Collectors.joining(",")));
    }


    @ExceptionHandler({RuntimeException.class, DataIntegrityViolationException.class, ExpiredJwtException.class, ServletException.class, IOException.class})
    public RestResult runtimeExceptionHandler(Throwable e) {
        e.printStackTrace();
        return RestResultGenerator.error(e.getMessage());
    }

    @ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public RestResult bindExceptionHandler(BindException e) {
        return RestResultGenerator.error("参数绑定异常：" + e.getFieldErrors().stream().map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage()).collect(Collectors.joining(",")));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public RestResult badCredentialsExceptionHandler(BadCredentialsException e) {
        return RestResultGenerator.error(e.getMessage());
    }
}
