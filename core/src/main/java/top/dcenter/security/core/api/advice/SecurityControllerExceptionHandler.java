package top.dcenter.security.core.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.dcenter.security.core.exception.IllegalAccessUrlException;
import top.dcenter.security.core.exception.ParameterErrorException;
import top.dcenter.security.core.exception.UserNotExistException;
import top.dcenter.security.core.exception.ValidateCodeException;
import top.dcenter.security.core.exception.ValidateCodeParamErrorException;
import top.dcenter.security.core.exception.ValidateCodeProcessException;
import top.dcenter.security.core.vo.ResponseResult;

/**
 * 核心错误处理器,如需自定义，继承此类并注入 IOC 容器即可
 * @author zhailiang
 * @medifiedBy  zyw
 * @version V1.0  Created by 2020/5/2 15:35
 */
@Slf4j
public class SecurityControllerExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleUserNotException(UserNotExistException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseResult.fail(ex.getErrorCodeEnum().getCode(), message, ex.getId());
    }

    @ExceptionHandler(ParameterErrorException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult parameterErrorException(ParameterErrorException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseResult.fail(ex.getErrorCodeEnum().getCode(), message, ex.getData());
    }
    
    @ExceptionHandler(ValidateCodeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult validateCodeException(ValidateCodeException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseResult.fail(ex.getErrorCodeEnum().getCode(), message);
    }

    @ExceptionHandler(ValidateCodeParamErrorException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult validateCodeParamErrorException(ValidateCodeParamErrorException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseResult.fail(ex.getErrorCodeEnum().getCode(), message, ex.getData());
    }

    @ExceptionHandler(ValidateCodeProcessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult validateCodeProcessException(ValidateCodeProcessException ex) {
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseResult.fail(ex.getErrorCodeEnum().getCode(), message);
    }
    @ExceptionHandler(IllegalAccessUrlException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult illegalAccessUrlException(IllegalAccessUrlException ex) {
        String errorMsg = ex.getMessage();
        log.error(errorMsg, ex);
        return ResponseResult.fail(ex.getErrorCodeEnum().getCode(), errorMsg);
    }

}