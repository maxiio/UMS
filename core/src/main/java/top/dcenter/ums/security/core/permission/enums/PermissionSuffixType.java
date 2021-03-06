package top.dcenter.ums.security.core.permission.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 权限后缀类型
 * @author zyw
 * @version V1.0  Created by 2020/9/17 9:33
 */
public enum PermissionSuffixType {
    /**
     * 查询
     */
    LIST("GET")
            {
                @Override
                public String getPermissionSuffix() {
                    return ":list";
                }
            },
    /**
     * 添加
     */
    ADD("POST")
            {
                @Override
                public String getPermissionSuffix() {
                    return ":add";
                }
            },
    /**
     * 更新
     */
    EDIT("PUT")
            {
                @Override
                public String getPermissionSuffix() {
                    return ":edit";
                }
            },
    /**
     * 删除
     */
    DELETE("DELETE")
            {
                @Override
                public String getPermissionSuffix() {
                    return ":del";
                }
            };

    /**
     * request method
     */
    @Getter
    private String method;

    PermissionSuffixType(String method) {
        this.method = method;
    }

    /**
     * 获取权限后缀
     * @return 返回权限后缀
     */
    public abstract String getPermissionSuffix();

    /**
     * 根据 requestMethod 获取权限后缀
     * @param method    requestMethod
     * @return  权限后缀, 如果 method 不匹配, 返回 null
     */
    public static String getPermissionSuffix(String method) {
        Objects.requireNonNull(method, "method require non null");
        PermissionSuffixType[] types = values();
        for (int i = 0, length = types.length; i < length; i++)
        {
            if (types[i].method.equals(method.toUpperCase()))
            {
                return types[i].getPermissionSuffix();
            }
        }
        return null;
    }

}
