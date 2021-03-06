package top.dcenter.ums.security.social.repository.jdbc.factory;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import top.dcenter.ums.security.social.api.repository.UsersConnectionRepositoryFactory;
import top.dcenter.ums.security.social.properties.SocialProperties;
import top.dcenter.ums.security.social.repository.jdbc.JdbcConnectionDataRepository;
import top.dcenter.ums.security.social.repository.jdbc.OAuthJdbcUsersConnectionRepository;

import javax.sql.DataSource;

/**
 * UsersConnectionRepositoryFactory 接口实现，
 * 用户需要对第三方{@link SocialProperties} <i>tableName</i> 用户表更改或者更改Repository的实现方式（如更换Redis）时，要实现此接口
 * {@link UsersConnectionRepositoryFactory}
 * .<br><br>
 *     自定义的接口实现并注入 IOC 容器会自动覆盖此类
 * @author zyw
 * @version V1.0  Created by 2020/5/13 23:37
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuthJdbcUsersConnectionRepositoryFactory implements UsersConnectionRepositoryFactory {

    private final JdbcConnectionDataRepository jdbcConnectionDataRepository;

    public OAuthJdbcUsersConnectionRepositoryFactory(JdbcConnectionDataRepository jdbcConnectionDataRepository) {
        this.jdbcConnectionDataRepository = jdbcConnectionDataRepository;
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(DataSource dataSource,
                                                                  ConnectionFactoryLocator connectionFactoryLocator,
                                                                  TextEncryptor textEncryptor,
                                                                  SocialProperties socialProperties,
                                                                  ConnectionSignUp connectionSignUp,
                                                                  Boolean autoSignIn) {
        OAuthJdbcUsersConnectionRepository usersConnectionRepository =
                new OAuthJdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, textEncryptor,
                                                       jdbcConnectionDataRepository, socialProperties);
        // 用于第三方登录自动注册为用户功能的开关，当传 null 值时关闭自动注册，当不为 null 且 autoSignIn=true 时开启自动注册功能。
        // 需自己实现 ConnectionSignUp ，功能为从第三方的 connection 中获取用户唯一标识。
        if (autoSignIn && connectionSignUp != null)
        {
            usersConnectionRepository.setConnectionSignUp(connectionSignUp);
        }
        return usersConnectionRepository;
    }
}
