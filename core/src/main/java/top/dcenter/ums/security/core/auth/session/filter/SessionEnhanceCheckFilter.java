package top.dcenter.ums.security.core.auth.session.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import top.dcenter.ums.security.core.api.authentication.handler.BaseAuthenticationFailureHandler;
import top.dcenter.ums.security.core.api.config.HttpSecurityAware;
import top.dcenter.ums.security.core.api.session.SessionEnhanceCheckService;
import top.dcenter.ums.security.core.api.session.strategy.EnhanceConcurrentControlAuthenticationStrategy;
import top.dcenter.ums.security.core.config.SecurityCoreAutoConfigurer;
import top.dcenter.ums.security.core.exception.SessionEnhanceCheckException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static top.dcenter.ums.security.core.consts.SecurityConstants.HEADER_USER_AGENT;
import static top.dcenter.ums.security.core.consts.SecurityConstants.SERVLET_CONTEXT_AUTHORIZE_REQUESTS_MAP_KEY;
import static top.dcenter.ums.security.core.consts.SecurityConstants.SESSION_ENHANCE_CHECK_KEY;
import static top.dcenter.ums.security.core.enums.ErrorCodeEnum.SESSION_ENHANCE_CHECK;

/**
 * session 增强检测, 如对客户端特征码检测, 增强对 session 攻击的防御. <br>
 * 如果要修改检测逻辑, 实现 {@link SessionEnhanceCheckService}, 注入 IOC 容器即可
 * 依赖 {@link EnhanceConcurrentControlAuthenticationStrategy}. <br><br>
 *     属性 authorizeRequestMap 通过 {@link SecurityCoreAutoConfigurer} 方法
 *     groupingAuthorizeRequestUris() 注入
 * @author zyw
 * @version V1.0  Created by 2020/6/2 10:14
 */
@Slf4j
public class SessionEnhanceCheckFilter extends OncePerRequestFilter {

    private final BaseAuthenticationFailureHandler baseAuthenticationFailureHandler;
    private SessionEnhanceCheckService sessionEnhanceCheckService;
    /**
     * 通过 {@link SecurityCoreAutoConfigurer} 注入.
     */
    private Map<String, Set<String>> authorizeRequestMap;
    private final AntPathMatcher pathMatcher;

    public SessionEnhanceCheckFilter(BaseAuthenticationFailureHandler baseAuthenticationFailureHandler,
                                     SessionEnhanceCheckService sessionEnhanceCheckService) {

        this.baseAuthenticationFailureHandler = baseAuthenticationFailureHandler;
        this.sessionEnhanceCheckService = sessionEnhanceCheckService;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (this.sessionEnhanceCheckService != null && session != null && !isPermitUri(request.getRequestURI(), session))
        {
            // 用户 client 的特征值
            String checkValue = (String) session.getAttribute(SESSION_ENHANCE_CHECK_KEY);
            // 如果不符合特征值, 则为非法 session, 直接返回登录页面
            if (checkValue != null && !this.sessionEnhanceCheckService.sessionEnhanceCheck(checkValue, request))
            {
                log.warn("session被劫持: ip={}, ua={}, sid={}, uri={}, checkValue={}",
                         request.getRemoteAddr(),
                         request.getHeader(HEADER_USER_AGENT),
                         session.getId(),
                         request.getRequestURI(),
                         checkValue);
                this.baseAuthenticationFailureHandler.onAuthenticationFailure(request, response,
                                                                              new SessionEnhanceCheckException(SESSION_ENHANCE_CHECK, session.getId(), checkValue));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPermitUri(String requestUri, HttpSession session) {
        // authorizeRequestMap 通过 SecurityCoreAutoConfigurer.groupingAuthorizeRequestUris(..) 注入 ServletContext,
        // 首次访问时从 ServletContext 赋值
        if (MapUtils.isEmpty(this.authorizeRequestMap))
        {

            // noinspection unchecked
            this.authorizeRequestMap =
                    Objects.requireNonNullElse((Map<String, Set<String>>) session.getServletContext().getAttribute(SERVLET_CONTEXT_AUTHORIZE_REQUESTS_MAP_KEY), new HashMap<>(0));
        }
        Set<String> permitSet =
                Objects.requireNonNullElse(this.authorizeRequestMap.get(HttpSecurityAware.PERMIT_ALL), new HashSet<>());
        for (String permitUri : permitSet)
        {
            if (this.pathMatcher.match(permitUri, requestUri))
            {
                return true;
            }
        }
        return false;
    }

}
