package top.dcenter.security.core.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import top.dcenter.security.core.enums.ErrorCodeEnum;

/**
 * 验证码参数异常
 * @author zhailiang
 * @medifiedBy  zyw
 * @version V1.0  Created by 2020/5/7 13:19
 */
public class ValidateCodeParamErrorException extends AuthenticationException {

    private static final long serialVersionUID = 5071331297299386304L;
    @Getter
    private ErrorCodeEnum errorCodeEnum;
    @Getter
    private String data;

    public ValidateCodeParamErrorException(ErrorCodeEnum errorCodeEnum, String data) {
        super(errorCodeEnum.getMsg());
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
    }

    public ValidateCodeParamErrorException(ErrorCodeEnum errorCodeEnum, Throwable cause, String data) {
        super(errorCodeEnum.getMsg(), cause);
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
    }
}