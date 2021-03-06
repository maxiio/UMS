package demo.security.permission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.dcenter.ums.security.core.permission.annotation.UriAuthorize;
import top.dcenter.ums.security.core.permission.config.EnableUriAuthorize;
import top.dcenter.ums.security.core.permission.config.UriAuthorizeInterceptorAutoConfiguration;

/**
 * &#64;PreAuthorize 注解需要 @EnableGlobalMethodSecurity(prePostEnabled = true) 支持, 在 @EnableUriAuthorize 中
 * {@link UriAuthorizeInterceptorAutoConfiguration}已配置, 不需要再次配置. <br>
 * &#64;UriAuthorize 注解需要 @EnableUriAuthorize(filterOrInterceptor = false) 支持, filterOrInterceptor=false 时为拦截器(注解方式)模式; filterOrInterceptor=true 时为过滤器模式.
 * @author zyw
 * @version V1.0  Created by 2020/9/9 22:49
 */
@RestController
@Slf4j
@EnableUriAuthorize()
public class PermissionController {
    /**
     * 此 uri 已经设置 PERMIT_ALL, 不用登录验证
     * 测试有 /test/permission:add 权限, 放行
     */
    @UriAuthorize("/test/permission:add")
    @GetMapping("/test/permission/{id}")
    public String testPermission(@PathVariable("id") String id) {
        return "test permission: " + id;
    }


    /**
     * 此 uri 已经设置 PERMIT_ALL, 不用登录验证
     * 测试不匹配 /test/deny:add 权限, 禁止访问
     */
    @UriAuthorize("/test/deny:add")
    @GetMapping("/test/deny/{id}")
    public String testDeny(@PathVariable("id") String id) {
        return "test deny: " + id;
    }

    /**
     * 此 uri 已经设置 PERMIT_ALL, 不用登录验证
     * 没有注解 @UriAuthorize 直接放行
     */
    @GetMapping("/test/pass/{id}")
    public String testPass(@PathVariable("id") String id) {
        return "test pass: " + id;
    }

    /**
     * 需要登录验证, 用户的 AuthorityList("admin, ROLE_USER")
     * 有注解 @PreAuthorize("HAS_ROLE('admin')") 没有 admin role, 禁止访问
     */
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test/role/{id}")
    public String testRole(@PathVariable("id") String id) {
        return "test role: " + id;
    }

    /**
     * 需要登录验证, 用户的 AuthorityList("admin, ROLE_USER")
     * 有注解 @PreAuthorize("HAS_ROLE('USER')"), 有 USER role, 直接放行
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/test/role2/{id}")
    public String testRole2(@PathVariable("id") String id) {
        return "test role2: " + id;
    }

    /**
     * 需要登录验证, 用户的 AuthorityList("admin, ROLE_USER")
     * 有注解 @PreAuthorize("HAS_AUTHORITY('admin')"), 有 admin authority, 直接放行
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/test/role3/{id}")
    public String testRole3(@PathVariable("id") String id) {
        return "test role3: " + id;
    }

}
