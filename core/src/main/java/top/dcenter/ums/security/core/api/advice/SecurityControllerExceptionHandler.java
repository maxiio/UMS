package top.dcenter.ums.security.core.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.dcenter.ums.security.core.exception.ExpiredSessionDetectedException;
import top.dcenter.ums.security.core.exception.IllegalAccessUrlException;
import top.dcenter.ums.security.core.exception.ParameterErrorException;
import top.dcenter.ums.security.core.exception.UserNotExistException;
import top.dcenter.ums.security.core.exception.ValidateCodeException;
import top.dcenter.ums.security.core.exception.ValidateCodeParamErrorException;
import top.dcenter.ums.security.core.exception.ValidateCodeProcessException;
import top.dcenter.ums.security.core.vo.ResponseResult;

/**
 * 核心错误处理器,如需自定义，继承此类并注入 IOC 容器即可
 * @author zhailiang
 * @author  zyw
 * @version V1.0  Created by 2020/5/2 15:35
 */
public class SecurityControllerExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleUserNotException(UserNotExistException ex) {
        String message = ex.getMessage();
        return ResponseResult.fail(message, ex.getErrorCodeEnum(), ex.getUid());
    }

    @ExceptionHandler(ParameterErrorException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult parameterErrorException(ParameterErrorException ex) {
        String message = ex.getMessage();
        return ResponseResult.fail(message, ex.getErrorCodeEnum(), ex.getData());
    }
    
    @ExceptionHandler(ValidateCodeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult validateCodeException(ValidateCodeException ex) {
        String message = ex.getMessage();
        return ResponseResult.fail(message, ex.getErrorCodeEnum());
    }

    @ExceptionHandler(ValidateCodeParamErrorException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult validateCodeParamErrorException(ValidateCodeParamErrorException ex) {
        String message = ex.getMessage();
        return ResponseResult.fail(message, ex.getErrorCodeEnum(), ex.getData());
    }

    @ExceptionHandler(ValidateCodeProcessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult validateCodeProcessException(ValidateCodeProcessException ex) {
        String message = ex.getMessage();
        return ResponseResult.fail(message, ex.getErrorCodeEnum());
    }
    @ExceptionHandler(IllegalAccessUrlException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult illegalAccessUrlException(IllegalAccessUrlException ex) {
        String errorMsg = ex.getMessage();
        return ResponseResult.fail(errorMsg, ex.getErrorCodeEnum(), ex.getData());
    }

    @ExceptionHandler(ExpiredSessionDetectedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult expiredSessionDetectedException(ExpiredSessionDetectedException ex) {
        String errorMsg = ex.getMessage();
        return ResponseResult.fail(errorMsg, ex.getErrorCodeEnum());
    }

}