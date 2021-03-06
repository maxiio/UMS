package top.dcenter.ums.security.social.view;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.ui.Model;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.AbstractView;
import top.dcenter.ums.security.social.api.banding.ShowConnectionStatusViewService;
import top.dcenter.ums.security.social.banding.DefaultShowConnectionStatusViewServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 查看用户所有的第三方登录数据, 返回格式为 Json.<br><br>
 *     想更改视图的回显内容，实现接口 {@link ShowConnectionStatusViewService}, 并且注入 IOC 容器即可，自动会替换
 *     {@link DefaultShowConnectionStatusViewServiceImpl}。
 * @see ConnectController#connectionStatus(NativeWebRequest, Model)
 * @author zhailiang
 */
@Slf4j
public class ConnectionStatusView extends AbstractView {
	
	private final ShowConnectionStatusViewService showConnectionStatusViewService;

	public ConnectionStatusView(ShowConnectionStatusViewService showConnectionStatusViewService) {
		this.showConnectionStatusViewService = showConnectionStatusViewService;
		setContentType(MediaType.TEXT_HTML_VALUE);
	}

	/**
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request,
	                                       @NotNull HttpServletResponse response) throws Exception {

		showConnectionStatusViewService.renderMergedOutputModel(model, request, response);
	}

}
