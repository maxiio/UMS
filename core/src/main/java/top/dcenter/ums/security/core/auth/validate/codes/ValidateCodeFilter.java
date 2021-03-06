package top.dcenter.ums.security.core.auth.validate.codes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import top.dcenter.ums.security.core.api.authentication.handler.BaseAuthenticationFailureHandler;
import top.dcenter.ums.security.core.api.validate.code.ValidateCodeProcessor;
import top.dcenter.ums.security.core.enums.ErrorCodeEnum;
import top.dcenter.ums.security.core.exception.AbstractResponseJsonAuthenticationException;
import top.dcenter.ums.security.core.exception.ValidateCodeException;
import top.dcenter.ums.security.core.properties.ValidateCodeProperties;
import top.dcenter.ums.security.core.util.ConvertUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static top.dcenter.ums.security.core.consts.SecurityConstants.GET_METHOD;
import static top.dcenter.ums.security.core.enums.ErrorCodeEnum.ILLEGAL_VALIDATE_CODE_TYPE;


/**
 * 验证码过滤器
 * @author zhailiang
 * @author  zyw
 * @version V1.0  Created by 2020/5/4 9:29
 */
@Slf4j
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private final ValidateCodeProperties validateCodeProperties;
    private final BaseAuthenticationFailureHandler baseAuthenticationFailureHandler;

    private final AntPathMatcher pathMatcher;
    /**
     * 验证码处理器 Holder
     */
    private final ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     *  验证码认证 map&#60;uri, validateCodeType&#62;
     */
    @Getter
    private Map<String, ValidateCodeType> authUrlMap = new HashMap<>();

    public ValidateCodeFilter(ValidateCodeProcessorHolder validateCodeProcessorHolder,
                              BaseAuthenticationFailureHandler baseAuthenticationFailureHandler,
                              ValidateCodeProperties validateCodeProperties) {
        this.baseAuthenticationFailureHandler = baseAuthenticationFailureHandler;
        pathMatcher = new AntPathMatcher();
        this.validateCodeProcessorHolder = validateCodeProcessorHolder;
        this.validateCodeProperties = validateCodeProperties;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        // 添加图片验证码 urls
        ConvertUtil.list2Map(validateCodeProperties.getImage().getAuthUrls(), ValidateCodeType.IMAGE, authUrlMap);
        // 添加滑块验证码 urls
        ConvertUtil.list2Map(validateCodeProperties.getSlider().getAuthUrls(), ValidateCodeType.SLIDER, authUrlMap);
        // 添加轨迹验证码 urls
        ConvertUtil.list2Map(validateCodeProperties.getTrack().getAuthUrls(), ValidateCodeType.TRACK, authUrlMap);
        // 添加选择类验证码 urls
        ConvertUtil.list2Map(validateCodeProperties.getSelection().getAuthUrls(), ValidateCodeType.SELECTION, authUrlMap);
        // 添加自定义验证码 urls
        ConvertUtil.list2Map(validateCodeProperties.getCustomize().getAuthUrls(), ValidateCodeType.CUSTOMIZE, authUrlMap);
        // 添加短信验证码 urls
        ConvertUtil.list2Map(validateCodeProperties.getSms().getAuthUrls(), ValidateCodeType.SMS, authUrlMap);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String requestUri = request.getRequestURI();
        // 验证码逻辑，当短信验证码与图片验证码 url 相同时，优先使用短信验证码逻辑。
        ValidateCodeType validateCodeType = getValidateCodeType(request);

        String ip = request.getRemoteAddr();
        String sid = request.getSession(true).getId();

        try {
            if (validateCodeType != null)
            {
                String typeName = validateCodeType.name();
                ValidateCodeProcessor validateCodeProcessor = validateCodeProcessorHolder.findValidateCodeProcessor(validateCodeType);
                if (validateCodeProcessor != null)
                {
                    validateCodeProcessor.validate(new ServletWebRequest(request, response));

                } else
                {
                    log.warn("违法的验证码类型: error={}, ip={}, sid={}, type={}",
                                 ILLEGAL_VALIDATE_CODE_TYPE.getMsg(),
                             ip,
                             sid,
                             typeName);
                    throw new ValidateCodeException(ILLEGAL_VALIDATE_CODE_TYPE, ip,
                                                    typeName);
                }
            }

        } catch (Exception e) {
            log.warn("验证码错误: error={}, ip={}, sid={}, uri={}",
                     e.getMessage(),
                     ip,
                     sid,
                     requestUri);

            AbstractResponseJsonAuthenticationException ex;
            if (e instanceof AbstractResponseJsonAuthenticationException)
            {
                ex = (AbstractResponseJsonAuthenticationException) e;
            }
            else
            {
                ex = new ValidateCodeException(ErrorCodeEnum.VALIDATE_CODE_ERROR, e, ip, validateCodeType.name());
            }
            baseAuthenticationFailureHandler.onAuthenticationFailure(request, response, ex);
            return;
        }

        doFilter(request, response, filterChain);
    }

    /**
     * 获取验证码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request   HttpServletRequest
     * @return  ValidateCodeType
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result;
        String method = request.getMethod();
        if (!StringUtils.equalsIgnoreCase(method, GET_METHOD)) {
            String requestUri = request.getRequestURI();
            result = authUrlMap.getOrDefault(requestUri, null);
            if (result != null)
            {
                return result;
            }

            for (Map.Entry<String, ValidateCodeType> next : authUrlMap.entrySet())
            {
                if (pathMatcher.match(next.getKey(), requestUri))
                {
                    return next.getValue();
                }
            }
        }
        return null;
    }

}
