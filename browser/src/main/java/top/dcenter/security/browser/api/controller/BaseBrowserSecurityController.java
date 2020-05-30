package top.dcenter.security.browser.api.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 网页端 url 认证与授权的路由控制接口, 实现此接口并注册到 IOC 容器，则会替换
 * {@link BrowserSecurityController} 类
 * @author zyw23
 * @version V1.0
 * Created by 2020/5/22 17:26
 */
public interface BaseBrowserSecurityController {

    /**
     * 当需要身份认证时，跳转到这里, 根据不同 uri(支持通配符) 跳转到不同的认证入口.<br>
     * 必须添加注解：<pre>
     *     \@RequestMapping(DEFAULT_UNAUTHENTICATION_URL)
     *     public void requireAuthentication(HttpServletRequest request, HttpServletResponse response) {
     *          ...
     *     }
     * </pre>
     * @param request
     * @param response
     * @version V1.0  Created by 2020/5/3 17:43
     */
    void requireAuthentication(HttpServletRequest request, HttpServletResponse response);

}