package top.dcenter.ums.security.core.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import top.dcenter.ums.security.core.api.authentication.handler.BaseAuthenticationFailureHandler;
import top.dcenter.ums.security.core.api.authentication.handler.BaseAuthenticationSuccessHandler;
import top.dcenter.ums.security.core.api.service.AbstractUserDetailsService;
import top.dcenter.ums.security.core.api.session.SessionEnhanceCheckService;
import top.dcenter.ums.security.core.auth.mobile.SmsCodeLoginAuthenticationFilter;
import top.dcenter.ums.security.core.auth.mobile.SmsCodeLoginAuthenticationProvider;
import top.dcenter.ums.security.core.properties.ClientProperties;
import top.dcenter.ums.security.core.properties.SmsCodeLoginAuthenticationProperties;
import top.dcenter.ums.security.core.properties.ValidateCodeProperties;

import java.util.UUID;


/**
 * 短信登录配置
 * @author  zyw
 * @version V1.0  Created by 2020/5/7 23:32
 */
@SuppressWarnings("jol")
@Configuration
@ConditionalOnProperty(prefix = "security.mobile.login", name = "sms-code-login-is-open", havingValue = "true")
@AutoConfigureAfter({SecurityAutoConfiguration.class})
public class SmsCodeLoginAutoAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ValidateCodeProperties validateCodeProperties;
    private final BaseAuthenticationFailureHandler baseAuthenticationFailureHandler;
    private final BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private AbstractUserDetailsService userDetailsService;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false)
    private SessionRegistry sessionRegistry;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false)
    private SessionEnhanceCheckService sessionEnhanceCheckService;
    private String key;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false)
    private PersistentTokenRepository persistentTokenRepository;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private ClientProperties clientProperties;
    private final SmsCodeLoginAuthenticationProperties smsCodeLoginAuthenticationProperties;
    private final ObjectMapper objectMapper;

    public SmsCodeLoginAutoAuthenticationConfigurer(ValidateCodeProperties validateCodeProperties,
                                                    BaseAuthenticationFailureHandler baseAuthenticationFailureHandler,
                                                    BaseAuthenticationSuccessHandler baseAuthenticationSuccessHandler,
                                                    SmsCodeLoginAuthenticationProperties smsCodeLoginAuthenticationProperties,
                                                    ObjectMapper objectMapper) {
        this.validateCodeProperties = validateCodeProperties;
        this.baseAuthenticationFailureHandler = baseAuthenticationFailureHandler;
        this.baseAuthenticationSuccessHandler = baseAuthenticationSuccessHandler;
        this.smsCodeLoginAuthenticationProperties = smsCodeLoginAuthenticationProperties;
        this.objectMapper = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void configure(HttpSecurity http) {

        SmsCodeLoginAuthenticationFilter smsCodeLoginAuthenticationFilter =
                new SmsCodeLoginAuthenticationFilter(validateCodeProperties, smsCodeLoginAuthenticationProperties, objectMapper);
        smsCodeLoginAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeLoginAuthenticationFilter.setAuthenticationSuccessHandler(baseAuthenticationSuccessHandler);
        smsCodeLoginAuthenticationFilter.setAuthenticationFailureHandler(baseAuthenticationFailureHandler);

        if (persistentTokenRepository != null)
        {
            PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
                    new PersistentTokenBasedRememberMeServices(getKey(), userDetailsService,
                                                               persistentTokenRepository);
            persistentTokenBasedRememberMeServices.setTokenValiditySeconds(Integer.parseInt(String.valueOf(clientProperties.getRememberMe().getRememberMeTimeout().getSeconds())));
            persistentTokenBasedRememberMeServices.setParameter(clientProperties.getRememberMe().getRememberMeCookieName());
            // 添加rememberMe功能配置
            smsCodeLoginAuthenticationFilter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        }


        SmsCodeLoginAuthenticationProvider smsCodeLoginAuthenticationProvider = new SmsCodeLoginAuthenticationProvider(userDetailsService);
        http.authenticationProvider(smsCodeLoginAuthenticationProvider)
            .addFilterAfter(smsCodeLoginAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);


    }

    /**
     * Gets the key to use for validating remember me tokens. Either the value passed into
     * {@link #key(String)}, or a secure random string if none was specified.
     *
     * @return the remember me key to use
     */
    private String getKey() {
        if (this.key == null) {
            this.key = UUID.randomUUID().toString();
        }
        return this.key;
    }

    /**
     * Sets the key to identify tokens created for remember me auth. Default is
     * a secure randomly generated key.
     *
     * @param key the key to identify tokens created for remember me auth
     */
    public void key(String key) {
        this.key = key;
    }
}
