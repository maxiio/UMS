package top.dcenter.security.social;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * social 第三方登录属性
 *
 * @author zhailiang
 * @version V1.0  Created by 2020/5/3 19:52
 * @medifiedBy zyw
 */
@SuppressWarnings("jol")
@Getter
@Setter
@ConfigurationProperties("security.social")
public class SocialProperties {

    private QqProperties qq = new QqProperties();

    /**
     * 第三方登录回调处理 url ，也是 RedirectUrl 的前缀，默认为 /auth
     */
    private String filterProcessesUrl = "/auth";
    /**
     * 第三方登录回调的域名, 例如：https://localhost 默认为 "http://127.0.0.1"，
     * redirectUrl 直接由 domain/filterProcessesUrl/(security.social.providerId 中的 providerId 组成：如 qq、wechat)组成
     */
    private String domain = "http://127.0.0.1";

    /**
     * QQ 登录时是否自动注册：如果为 true 且实现 ConnectionSignUp 接口则自动登录，而且 singUpUrl 失效，否则不自动登录, 默认为 true
     */
    private Boolean autoSignIn = true;
    /**
     * 第三方登录用户授权成功且未注册过的，则跳转的注册页面， 默认为 /signUp.html，
     * autoSignIn=true 且实现 ConnectionSignUp 接口则自动登录时 singUpUrl 会失效
     */
    private String singUpUrl = "/singUp.html";
    /**
     * 第三方登录用户授权失败跳转页面， 默认为 "/signIn.html"。这个模块暂时没实现，
     */
    private String failureUrl = "/singIn.html";

    /**
     * 第三方登录用户数据库表的字段 key 与 secret 加密专用密码
     */
    private String textEncryptorPassword = "7ca5d913a17b4942942d16a974e3fecc";
    /**
     * 第三方登录用户数据库表的字段 key 与 secret 加密专用密码
     */
    private String textEncryptorSalt = "cd538b1b077542aca5f86942b6507fe2";


    /* ==========================自定义第三方登录用户表及相关 CURD 语句============================= */
    /**
     * 第三方登录用户数据库表前缀<br>
     *      注意：表名前缀要与 tableName 相同。
     */
    private String tablePrefix = "social_";
    /**
     * 第三方登录用户数据库表名称，<br>
     *     注意：表名前缀要与 tablePrefix 相同。
     */
    private String tableName = "social_UserConnection";

    /**
     * 第三方登录用户数据库用户表用户唯一 ID 字段名称， 默认为 userId
     */
    private String userIdColumnName = "userId";
    /**
     * 第三方登录用户数据库用户表服务商 providerId 字段名称， 默认为 providerId
     */
    private String providerIdColumnName = "providerId";
    /**
     * 第三方登录用户数据库用户表服务商用户 providerUserId 字段名称， 默认为 providerUserId
     */
    private String providerUserIdColumnName = "providerUserId";
    /**
     * 第三方登录用户数据库用户表 rank 字段名称， 默认为 `rank`。<br>
     *     注意：因为 MySQL 8.0 版本 rank 是个关键字。一定要用 ` 包裹。
     */
    private String rankColumnName = "`rank`";

    /**
     * 第三方登录用户数据库用户表用户显示名称 displayName 字段名称， 默认为 displayName
     */
    private String displayNameColumnName = "displayName";
    /**
     * 第三方登录用户数据库用户表用户主页 profileUrl 字段名称， 默认为 profileUrl
     */
    private String profileUrlColumnName = "profileUrl";
    /**
     * 第三方登录用户数据库用户表用户头像 imageUrl 字段名称， 默认为 imageUrl
     */
    private String imageUrlColumnName = "imageUrl";
    /**
     * 第三方登录用户数据库用户表用户 accessToken 字段名称， 默认为 accessToken
     */
    private String accessTokenColumnName = "accessToken";
    /**
     * 第三方登录用户数据库用户表用户 secret 字段名称， 默认为 secret
     */
    private String secretColumnName = "secret";
    /**
     * 第三方登录用户数据库用户表用户显 refreshToken 字段名称， 默认为 refreshToken
     */
    private String refreshTokenColumnName = "refreshToken";
    /**
     * 第三方登录用户数据库用户表用户过期时间 expireTime 字段名称， 默认为 expireTime
     */
    private String expireTimeColumnName = "expireTime";

    /**
     * 第三方登录用户数据库用户表创建语句。 <br>
     * 修改第三方登录用户数据库用户表创建语句时，要注意：修改字段名称可以直接修改上面的字段名称即可，不用修改建表语句，不可以减少字段，但可以另外增加字段。<br>
     * 用户需要对第三方登录的用户表与 curd 的 sql 语句结构进行更改时（curd 语句更改功能暂时没实现），
     * 如果需要，请实现 UsersConnectionRepositoryFactory，可以参考 OAuth2UsersConnectionRepositoryFactory。<br>
     * 注意： sql 语句中的 %s 必须写上，且 %s 的顺序必须与后面的字段名称所对应的含义对应 :<br>
     * tableName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName、<br>
     * rankColumnName、<br>
     * displayNameColumnName、<br>
     * profileUrlColumnName、<br>
     * imageUrlColumnName、<br>
     * accessTokenColumnName、<br>
     * secretColumnName、<br>
     * refreshTokenColumnName、<br>
     * expireTimeColumnName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName、<br>
     * rankColumnName
     */
    private String creatUserConnectionTableSql = "create table %s (%s " +
            "varchar(255) not null,\n" +
            "\t%s varchar(255) not null,\n" +
            "\t%s varchar(255),\n" +
            "\t%s int not null,\n" +
            "\t%s varchar(255),\n" +
            "\t%s varchar(512),\n" +
            "\t%s varchar(512),\n" +
            "\t%s varchar(512) not null,\n" +
            "\t%s varchar(512),\n" +
            "\t%s varchar(512),\n" +
            "\t%s bigint,\n" +
            "\tprimary key (%s, %s, %s),\n" +
            "\tunique index UserConnectionRank(%s, %s, %s));";

    /**
     * 第三方登录用户数据库用户表查询 userIds 的查询语句。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * databaseName、<br>
     * tableName
     */
    private final String queryUserConnectionTableExistSql = "SELECT COUNT(1) FROM information_schema.tables WHERE " +
            "table_schema='%s' AND table_name = '%s'";


    /**
     * 第三方登录用户数据库用户表查询 userIds 的查询语句。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * userIdColumnName、<br>
     * tableName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName
     */
    private final String findUserIdsWithConnectionSql = "select %s from %s where %s = ? and %s = ?";

    /**
     * 通过第三方服务提供商提供的 providerId 与 providerUserIds 从数据库用户表查询 userIds 的查询语句。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * userIdColumnName、<br>
     * tableName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName
     */
    private final String findUserIdsConnectedToSql = "select %s from %S where %s = :%s and %s in (:%s)";

    /**
     * 通过第三方服务提供商提供的 providerId 与 providerUserIds 从数据库用户表查询 userIds 的查询语句。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * userIdColumnName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName、<br>
     * displayNameColumnName、<br>
     * profileUrlColumnName、<br>
     * imageUrlColumnName、<br>
     * accessTokenColumnName、<br>
     * secretColumnName、<br>
     * refreshTokenColumnName、<br>
     * expireTimeColumnName、<br>
     * tableName
     */
    private final String selectFromUserConnectionSql = "select %s, %s, %s, %s, %s, %s, %s, %s, %s, %s from %s";

    /**
     * 第三方登录用户数据库用户表更新语句。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * tableName、<br>
     * displayNameColumnName、<br>
     * profileUrlColumnName、<br>
     * imageUrlColumnName、<br>
     * accessTokenColumnName、<br>
     * secretColumnName、<br>
     * refreshTokenColumnName、<br>
     * expireTimeColumnName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName
     */
    private final String updateConnectionSql = "update %s " +
            "set %s = ?, " +
            "%s = ?, " +
            "%s = ?, " +
            "%s = ?, " +
            "%s = ?, " +
            "%s = ?, " +
            "%s = ? " +
            "where %s = ? and " +
            "%s = ? and " +
            "%s = ?";

    /**
     * 第三方登录用户数据库用户表添加用户语句。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * tableName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName、<br>
     * rankColumnName、<br>
     * displayNameColumnName、<br>
     * profileUrlColumnName、<br>
     * imageUrlColumnName、<br>
     * accessTokenColumnName、<br>
     * secretColumnName、<br>
     * refreshTokenColumnName、<br>
     * expireTimeColumnName
     */
    private final String addConnectionSql = "insert into %s(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * 第三方登录用户数据库用户表查询添加用户时的所需 rank 的值。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * rankColumnName、<br>
     * rankColumnName、<br>
     * tableName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName
     */
    private final String addConnectionQueryForRankSql = "select coalesce(max(%s) + 1, 1) as %s from %s where %s = ? and %s = ?";

    /**
     * 第三方登录用户数据库用户表根据 userId 与 providerId 删除多个用户。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * tableName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName
     */
    private final String removeConnectionsSql = "delete from %s where %s = ? and %s = ?";
    /**
     * 第三方登录用户数据库用户表根据 userId、providerId、providerUserId 删除一个用户。 <br>
     * 注意： sql 语句中的 %s 必须写上，%s按顺序会用对应的 :<br>
     * tableName、<br>
     * userIdColumnName、<br>
     * providerIdColumnName、<br>
     * providerUserIdColumnName
     */
    private final String removeConnectionSql = "delete from %s where %s = ? and %s = ? and %s = ?";



    public String getRemoveConnectionSql() {

        return String.format(removeConnectionSql,
                             tableName,
                             userIdColumnName,
                             providerIdColumnName,
                             providerUserIdColumnName);
    }

    public String getRemoveConnectionsSql() {

        return String.format(removeConnectionsSql,
                             tableName,
                             userIdColumnName,
                             providerIdColumnName);
    }

    public String getAddConnectionQueryForRankSql() {

        return String.format(addConnectionQueryForRankSql,
                             rankColumnName,
                             rankColumnName,
                             tableName,
                             userIdColumnName,
                             providerIdColumnName);
    }

    public String getAddConnectionSql() {

        return String.format(addConnectionSql,
                             tableName,
                             userIdColumnName,
                             providerIdColumnName,
                             providerUserIdColumnName,
                             rankColumnName,
                             displayNameColumnName,
                             profileUrlColumnName,
                             imageUrlColumnName,
                             accessTokenColumnName,
                             secretColumnName,
                             refreshTokenColumnName,
                             expireTimeColumnName);
    }

    public String getUpdateConnectionSql() {

        return String.format(updateConnectionSql,
                             tableName,
                             displayNameColumnName,
                             profileUrlColumnName,
                             imageUrlColumnName,
                             accessTokenColumnName,
                             secretColumnName,
                             refreshTokenColumnName,
                             expireTimeColumnName,
                             userIdColumnName,
                             providerIdColumnName,
                             providerUserIdColumnName);
    }

    public String getSelectFromUserConnectionSql() {
        return String.format(selectFromUserConnectionSql,
                             userIdColumnName,
                             providerIdColumnName,
                             providerUserIdColumnName,
                             displayNameColumnName,
                             profileUrlColumnName,
                             imageUrlColumnName,
                             accessTokenColumnName,
                             secretColumnName,
                             refreshTokenColumnName,
                             expireTimeColumnName,
                             tableName);
    }

    public String getCreatUserConnectionTableSql() {
        return String.format(creatUserConnectionTableSql,
                             tableName,
                             userIdColumnName,
                             providerIdColumnName,
                             providerUserIdColumnName,
                             rankColumnName,
                             displayNameColumnName,
                             profileUrlColumnName,
                             imageUrlColumnName,
                             accessTokenColumnName,
                             secretColumnName,
                             refreshTokenColumnName,
                             expireTimeColumnName,
                             userIdColumnName,
                             providerIdColumnName,
                             providerUserIdColumnName,
                             userIdColumnName,
                             providerIdColumnName,
                             rankColumnName);
    }

    public String getQueryUserConnectionTableExistSql(String databaseName) {

        return String.format(queryUserConnectionTableExistSql, databaseName, tableName);
    }

    public String getFindUserIdsWithConnectionSql() {

        return String.format(findUserIdsWithConnectionSql,
                             userIdColumnName,
                             tableName,
                             providerIdColumnName,
                             providerUserIdColumnName);
    }

    public String getFindUserIdsConnectedToSql() {

        return String.format(findUserIdsConnectedToSql,
                             userIdColumnName,
                             tableName,
                             providerIdColumnName,
                             providerIdColumnName,
                             providerUserIdColumnName,
                             providerIdColumnName);
    }

    @Getter
    @Setter
    public class QqProperties extends org.springframework.boot.autoconfigure.social.SocialProperties {
        /**
         * 服务提供商标识, 默认为 qq
         */
        private String providerId = "qq";
        /**
         * 回调地址(格式必须是：domain/filterProcessesUrl/providerId)，默认
         */
        private String redirectUrl = domain + "/" + filterProcessesUrl + "/" + providerId;

    }

}
