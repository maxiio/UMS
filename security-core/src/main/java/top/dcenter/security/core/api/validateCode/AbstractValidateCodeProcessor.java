package top.dcenter.security.core.api.validateCode;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import top.dcenter.security.core.excception.ValidateCodeException;
import top.dcenter.security.core.validate.code.ValidateCode;
import top.dcenter.security.core.validate.code.ValidateCodeType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 校验码处理逻辑的默认实现抽象类
 * @author zhailiang
 * @medifiedBy  zyw
 * @version V1.0  Created by 2020/5/6 10:14
 */
@Slf4j
public abstract class AbstractValidateCodeProcessor implements ValidateCodeProcessor {
    /**
     * 操作 session 的工具类
     */
    protected final SessionStrategy sessionStrategy;

    private final Map<String, ValidateCodeGenerator> validateCodeGenerators;


    public AbstractValidateCodeProcessor(Map<String, ValidateCodeGenerator> validateCodeGenerators) {
        this.sessionStrategy = new HttpSessionSessionStrategy();
        if (validateCodeGenerators == null)
        {
            this.validateCodeGenerators = new HashMap<>(0);
            return;
        }
        Collection<ValidateCodeGenerator> values = validateCodeGenerators.values();
        this.validateCodeGenerators =
                values.stream().collect(Collectors.toMap((validateCodeGenerator -> validateCodeGenerator.getValidateCodeType()),
                                                         validateCodeGenerator -> validateCodeGenerator));
    }

    @Override
    public final boolean produce(ServletWebRequest request) throws ValidateCodeException {
        ValidateCode validateCode;

        try
        {
            validateCode = generate(request);
            saveSession(request, validateCode);
            boolean validateStatus = sent(request, validateCode);
            if (!validateStatus)
            {
                this.sessionStrategy.removeAttribute(request, getValidateCodeType(getValidateCodeType()).getSessionKey());
                return false;
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            this.sessionStrategy.removeAttribute(request, getValidateCodeType(getValidateCodeType()).getSessionKey());
            throw new ValidateCodeException(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        try {
            ValidateCodeGenerator validateCodeGenerator = getValidateCodeGenerator(getValidateCodeType(getValidateCodeType()));
            return (ValidateCode) validateCodeGenerator.generate(request.getRequest());
        }
        catch (ValidateCodeException e) {
            throw new ValidateCodeException(e.getMessage(), e);
        }
        catch (Exception e) {
            throw new ValidateCodeException("获取验证码失败，请重试", e);
        }
    }

    @Override
    public boolean saveSession(ServletWebRequest request, ValidateCode validateCode) {
        try {
            ValidateCodeType validateCodeType = getValidateCodeType(getValidateCodeType());
            if (validateCodeType == null)
            {
                return false;
            }
            this.sessionStrategy.setAttribute(request, validateCodeType.getSessionKey(), validateCode);
        }
        catch (Exception e) {
            log.warn(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 发送验证码，由子类实现,
     * 发送失败，必须清除 sessionKey 的缓存，示例代码: <br>
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;sessionStrategy.removeAttribute(request, sessionKey); </p>
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;sessionKey 获取方式：</p>
     *        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *        如果不清楚是哪种类型 sessionKey，用如下方式：</p>
     *        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *        ValidateCodeType validateCodeType = getValidateCodeType();</p>
     *        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *        String sessionKey = validateCodeType.getSessionKey();</p>
     * @param request   ServletWebRequest
     * @param validateCode  校验码对象
     * @return  是否发送成功的状态
     */
    @Override
    public abstract boolean sent(ServletWebRequest request, ValidateCode validateCode);

    @Override
    public void validate(ServletWebRequest request) throws ServletRequestBindingException, ValidateCodeException {
        ValidateCodeType validateCodeType = getValidateCodeType(getValidateCodeType());
        String sessionKey = validateCodeType.getSessionKey();

        String requestParamValidateCodeName = getValidateCodeGenerator(validateCodeType).getRequestParamValidateCodeName();

        ValidateCode codeInSession = (ValidateCode) this.sessionStrategy.getAttribute(request, sessionKey);
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), requestParamValidateCodeName);
        if (!StringUtils.isNotBlank(codeInRequest))
        {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null)
        {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpired())
        {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest))
        {
            throw new ValidateCodeException("验证码不匹配");
        }
        sessionStrategy.removeAttribute(request, sessionKey);

    }

    /**
     * 获取校验码的类型,
     * 如果不存在，返回 null
     * @return  如果不存在，返回 null
     */
    protected ValidateCodeType getValidateCodeType(String type) {
        if (StringUtils.isNotBlank(type))
        {
            try {
                return ValidateCodeType.valueOf(type.toUpperCase());
            }
            catch (IllegalArgumentException e) { }
        }
        return null;
    }

    /**
     * 获取校验码类型
     * @return  ValidateCodeType 的小写字符串
     */
    @Override
    public abstract String getValidateCodeType();

    /**
     * 获取校验码生成器
     * @param type
     * @return
     * @throws ValidateCodeException
     */
    private ValidateCodeGenerator getValidateCodeGenerator(ValidateCodeType type) {
        try {
            if (validateCodeGenerators != null)
            {
                ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(type.name().toLowerCase());
                if (validateCodeGenerator != null)
                {
                    return validateCodeGenerator;
                }
            }
            throw new  ValidateCodeException("校验码生成失败");
        }
        catch (Exception e) {
            throw new ValidateCodeException("校验码类型错误", e);
        }
    }

}