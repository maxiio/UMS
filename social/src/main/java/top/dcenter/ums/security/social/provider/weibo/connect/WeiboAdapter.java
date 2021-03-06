package top.dcenter.ums.security.social.provider.weibo.connect;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import top.dcenter.ums.security.social.provider.weibo.api.Weibo;
import top.dcenter.ums.security.social.provider.weibo.api.WeiboUserInfo;

import java.io.IOException;

/**
 *
 * @author edva8332
 */
@Slf4j
public class WeiboAdapter implements ApiAdapter<Weibo> {

	/**
	 * 微博个人主页前缀
	 */
	public static final String PROFILE_URL_PREFIX = "http://weibo.com/u/";
	@Getter
	@Setter
	private String uid;

	@Override
	public boolean test(Weibo api) {
		return true;
	}

	@Override
	public void setConnectionValues(Weibo api, ConnectionValues values) {
		WeiboUserInfo weiboUserInfo = fetchWeiboUserInfo(api);
		values.setProviderUserId(String.valueOf(weiboUserInfo.getId()));
		values.setDisplayName(weiboUserInfo.getName());
		values.setProfileUrl(PROFILE_URL_PREFIX + weiboUserInfo.getId());
		values.setImageUrl(weiboUserInfo.getProfileImageUrl());
	}

	@Override
	public UserProfile fetchUserProfile(Weibo api) {
		WeiboUserInfo weiboUserInfo = fetchWeiboUserInfo(api);
		String name = weiboUserInfo.getName();
		return new UserProfileBuilder()
				.setId(weiboUserInfo.getIdstr())
				.setUsername(weiboUserInfo.getScreenName()).setName(name)
				.setLastName(extractChineseLastName(name))
				.setFirstName(extractChineseFirstname(name)).build();

	}

	private String extractChineseFirstname(String name) {
		String result = null;
		if (name != null && !name.trim().isEmpty()) {
			result = name.substring(1);
		}
		return result;
	}

	private String extractChineseLastName(String name) {
		String result = null;
		if (name != null && !name.trim().isEmpty()) {
			result = name.substring(0, 1);
		}
		return result;
	}

	private WeiboUserInfo fetchWeiboUserInfo(Weibo api) {
		WeiboUserInfo weiboUserInfo = null;
		try
		{
			weiboUserInfo = api.getUserInfo(uid);
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return weiboUserInfo;
	}

	@Override
	public void updateStatus(Weibo api, String message) {
		// do nothing
	}

}