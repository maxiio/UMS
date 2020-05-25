package top.dcenter.validate.code;

import lombok.extern.slf4j.Slf4j;
import top.dcenter.security.core.api.validate.code.SmsCodeSender;
import top.dcenter.security.core.properties.ValidateCodeProperties;
import top.dcenter.security.core.validate.code.ValidateCode;
import top.dcenter.security.core.validate.code.smscode.SmsCodeGenerator;

import javax.servlet.ServletRequest;

/**
 * 推荐实现此接口 {@link SmsCodeSender}。
 * @author zyw
 * @createrDate 2020-05-14 22:23
 */
//@Component()
@Slf4j
public class DemoSmsCodeGenerator extends SmsCodeGenerator {

    private final SmsCodeSender smsCodeSender;
    private final ValidateCodeProperties validateCodeProperties;

    public DemoSmsCodeGenerator(SmsCodeSender smsCodeSender, ValidateCodeProperties validateCodeProperties) {
        super(validateCodeProperties, smsCodeSender);
        this.smsCodeSender = smsCodeSender;
        this.validateCodeProperties = validateCodeProperties;
    }

    @Override
    public ValidateCode generate(ServletRequest request) {
        ValidateCode validateCode = smsCodeSender.getCode();
        if (log.isDebugEnabled())
        {
            log.debug("Demo =======>: {} = {}", this.validateCodeProperties.getSms().getRequestParamSmsCodeName(),
                      validateCode.getCode());
        }
        return validateCode;
    }

}