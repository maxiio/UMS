package top.dcenter.ums.security.social.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import top.dcenter.ums.security.core.auth.filter.AjaxOrFormRequestFilter;
import top.dcenter.ums.security.core.exception.AbstractResponseJsonAuthenticationException;
import top.dcenter.ums.security.core.properties.ClientProperties;
import top.dcenter.ums.security.social.properties.SocialProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static top.dcenter.ums.security.core.consts.SecurityConstants.HEADER_ACCEPT;
import static top.dcenter.ums.security.core.consts.SecurityConstants.HEADER_USER_AGENT;
import static top.dcenter.ums.security.core.util.AuthenticationUtil.authenticationFailureProcessing;
import static top.dcenter.ums.security.core.util.AuthenticationUtil.getAbstractResponseJsonAuthenticationException;

/**
 * 第三方授权登录错误处理器.<br><br>
 * 当用户注册异常 {@link AbstractResponseJsonAuthenticationException} 时返回 JSON 数据
 * @author zyw
 * @version V1.0  Created by 2020/5/4 13:46
 */
@Slf4j
public class SocialAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final ObjectMapper objectMapper;
    private final ClientProperties clientProperties;

    public SocialAuthenticationFailureHandler(ObjectMapper objectMapper, SocialProperties socialProperties, ClientProperties clientProperties) {
        this.objectMapper = objectMapper;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.clientProperties = clientProperties;
        setDefaultFailureUrl(socialProperties.getFailureUrl());
    }

    /**
     * {@link AbstractResponseJsonAuthenticationException} 类的异常, Response 会返回 Json 数据.
     * @param request request
     * @param response  response
     * @param exception {@link AbstractResponseJsonAuthenticationException} 类的异常, Response 会返回 Json 数据.
     * @throws IOException  IOException
     * @throws ServletException ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        AbstractResponseJsonAuthenticationException e = getAbstractResponseJsonAuthenticationException(exception);

        String reqData;
        if (request instanceof AjaxOrFormRequestFilter.AjaxOrFormRequest)
        {
            AjaxOrFormRequestFilter.AjaxOrFormRequest ajaxOrFormRequest =
                    ((AjaxOrFormRequestFilter.AjaxOrFormRequest) request);
            reqData = new String(ajaxOrFormRequest.getBody(), StandardCharsets.UTF_8);
        }
        else
        {
            reqData = request.getParameterMap().toString();
        }
        log.info("OAuth2登录失败: user={}, ip={}, ua={}, sid={}, reqData={}",
                 e == null ? null : e.getUid(),
                 request.getRemoteAddr(),
                 request.getHeader(HEADER_USER_AGENT),
                 request.getSession(true).getId(),
                 reqData);
        // 进行必要的缓存清理

        // 检测是否接收 json 格式
        String acceptHeader = request.getHeader(HEADER_ACCEPT);

        if (authenticationFailureProcessing(response, exception, e, acceptHeader, objectMapper, clientProperties))
        {
            // 进行必要的清理缓存
            SecurityContextHolder.clearContext();
            return;
        }

        super.onAuthenticationFailure(request, response, exception);
    }

}