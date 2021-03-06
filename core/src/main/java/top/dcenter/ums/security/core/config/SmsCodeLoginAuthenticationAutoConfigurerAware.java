package top.dcenter.ums.security.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import top.dcenter.ums.security.core.api.config.HttpSecurityAware;
import top.dcenter.ums.security.core.properties.SmsCodeLoginAuthenticationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 手机登录配置
 * @author zyw
 * @version V1.0  Created by 2020/5/15 21:51
 */
@Configuration
@ConditionalOnProperty(prefix = "security..mobile.login", name = "sms-code-login-is-open", havingValue = "true")
@AutoConfigureAfter({SmsCodeLoginAutoAuthenticationConfigurer.class})
@Slf4j
public class SmsCodeLoginAuthenticationAutoConfigurerAware implements HttpSecurityAware {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false)
    private SmsCodeLoginAutoAuthenticationConfigurer smsCodeLoginAutoAuthenticationConfigurer;
    private final SmsCodeLoginAuthenticationProperties smsCodeLoginAuthenticationProperties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SmsCodeLoginAuthenticationAutoConfigurerAware(SmsCodeLoginAuthenticationProperties smsCodeLoginAuthenticationProperties) {
        this.smsCodeLoginAuthenticationProperties = smsCodeLoginAuthenticationProperties;
    }

    @Override
    public void postConfigure(HttpSecurity http) {
        // dto nothing
    }

    @Override
    public void preConfigure(HttpSecurity http) throws Exception {
        // 短信验证码登录配置
        if (smsCodeLoginAutoAuthenticationConfigurer != null)
        {
            http.apply(smsCodeLoginAutoAuthenticationConfigurer);
        }
    }

    @Override
    public Map<String, Map<String, Set<String>>> getAuthorizeRequestMap() {
        final Map<String, Set<String>> permitAllMap = new HashMap<>(16);

        permitAllMap.put(smsCodeLoginAuthenticationProperties.getLoginProcessingUrlMobile(), null);

        Map<String, Map<String, Set<String>>> resultMap = new HashMap<>(1);

        resultMap.put(HttpSecurityAware.PERMIT_ALL, permitAllMap);

        return resultMap;
    }
}

