package top.dcenter.ums.security.core.api.controller;


import top.dcenter.ums.security.core.auth.controller.ClientSecurityController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 客户端 url 认证与授权的路由控制实现此接口并注册到 IOC 容器，则会替换
 * {@link ClientSecurityController} 类
 * @author zyw
 * @version V1.0
 * Created by 2020/5/22 17:26
 */
public interface BaseSecurityController {

    /**
     * 当需要身份认证时，跳转到这里, 根据不同 uri(支持通配符) 跳转到不同的认证入口.<br><br>
     * 必须添加注解：<pre>
     *     \@RequestMapping(loginUnAuthenticationUrl)
     *     public void requireAuthentication(HttpServletRequest request, HttpServletResponse response) {
     *          ...
     *     }
     * </pre>
     * @param request   request
     * @param response  response
     */
    void requireAuthentication(HttpServletRequest request, HttpServletResponse response);

}
