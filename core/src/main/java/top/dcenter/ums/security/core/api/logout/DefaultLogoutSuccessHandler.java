package top.dcenter.ums.security.core.api.logout;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import top.dcenter.ums.security.core.consts.SecurityConstants;
import top.dcenter.ums.security.core.enums.ErrorCodeEnum;
import top.dcenter.ums.security.core.properties.ClientProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static top.dcenter.ums.security.core.util.AuthenticationUtil.redirectProcessingLogoutByLoginProcessType;

/**
 * 登出成功处理器, 如要替换此类, 继承后注入 IOC 容器即可
 * @author zyw
 * @version V1.0  Created by 2020/6/4 23:20
 */
@Slf4j
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {

    protected final RedirectStrategy redirectStrategy;
    protected final ClientProperties clientProperties;
    protected final ObjectMapper objectMapper;
    @Autowired(required = false)
    protected UserCache userCache;

    public DefaultLogoutSuccessHandler(ClientProperties clientProperties, ObjectMapper objectMapper) {
        this.clientProperties = clientProperties;
        this.objectMapper = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.redirectStrategy = new DefaultRedirectStrategy();
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        HttpSession session = request.getSession(true);

        log.info("登出成功: user={}, ip={}, ua={}, sid={}, sck={}",
                 authentication != null ? authentication.getPrincipal() : "",
                 request.getRemoteAddr(),
                 request.getHeader(SecurityConstants.HEADER_USER_AGENT),
                 session.getId(),
                 session.getAttribute(SecurityConstants.SESSION_ENHANCE_CHECK_KEY));

        // 清理缓存
        session.removeAttribute(SecurityConstants.SESSION_ENHANCE_CHECK_KEY);
        if (userCache != null && authentication != null)
        {
            userCache.removeUserFromCache(authentication.getName());
        }

        redirectProcessingLogoutByLoginProcessType(request, response, clientProperties, objectMapper,
                                                   redirectStrategy, ErrorCodeEnum.LOGOUT_SUCCESS);
    }
}
