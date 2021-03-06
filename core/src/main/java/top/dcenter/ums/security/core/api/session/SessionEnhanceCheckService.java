package top.dcenter.ums.security.core.api.session;

import org.springframework.lang.NonNull;
import top.dcenter.ums.security.core.api.session.strategy.EnhanceConcurrentControlAuthenticationStrategy;
import top.dcenter.ums.security.core.auth.session.filter.SessionEnhanceCheckFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * session enhance check service, 比如对 client 端特征码的鉴别, , 增强 csrf 防范, 此接口应用在
 * {@link SessionEnhanceCheckFilter} 与
 * {@link EnhanceConcurrentControlAuthenticationStrategy}<br><br>
 *     实现此接口并注入 IOC 容器, 会根据实现的逻辑对 request 进行安全验证.
 * @author zyw
 * @version V1.0
 * Created by 2020/6/2 13:36
 */
public interface SessionEnhanceCheckService {

    /**
     * 从 request 中提取 client 段特征码, 存入 session 中
     * @param session   session
     * @param request   request
     */
    void setEnhanceCheckValue(@NonNull HttpSession session, @NonNull HttpServletRequest request);

    /**
     * session 中的特征码值 checkValue 与从 request 中提取 client 段特征码对比. 相同返回 true, 否则 false
     * @param checkValue 特征码值
     * @param request   request
     * @return 提取 checkValue 中的特征码与从 request 中提取 client 段特征码对比. 相同返回 true, 否则 false
     */
    boolean sessionEnhanceCheck(@NonNull String checkValue, @NonNull HttpServletRequest request);
}
