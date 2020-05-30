package top.dcenter.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import top.dcenter.security.core.api.config.SocialWebSecurityConfigurerAware;
import top.dcenter.security.social.api.config.SocialCoreConfig;
import top.dcenter.security.social.properties.SocialProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义 SocialWebSecurityConfigurerAware 接口
 * @see SocialWebSecurityConfigurerAware
 * @author zyw
 * @version V1.0  Created by 2020/5/12 12:02
 */
@Configuration
@Slf4j
public class DemoSocialSecurityConfigurerAware implements SocialWebSecurityConfigurerAware {

    private final SocialProperties socialProperties;

    private final SocialCoreConfig socialCoreConfig;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DemoSocialSecurityConfigurerAware(SocialProperties socialProperties,
                                             SocialCoreConfig socialCoreConfig) {
        this.socialProperties = socialProperties;
        this.socialCoreConfig = socialCoreConfig;
    }

    @Override
    public void postConfigure(HttpSecurity http) throws Exception {
        // do nothing
    }

    @Override
    public void preConfigure(HttpSecurity http) throws Exception {
        // do nothing
    }

    @Override
    public Map<String, Set<String>> getAuthorizeRequestMap() {
        Set<String> uriSet = new HashSet<>();
        uriSet.add("/user/testWebSecurityPostConfigurer");
        Map<String, Set<String>> authorizeRequestMap = new HashMap<>(1);
        authorizeRequestMap.put(permitAll, uriSet);
        log.info("Demo ======>: DemoSocialSecurityConfigurerSocial.getAuthorizeRequestMap");
        return authorizeRequestMap;
    }
}