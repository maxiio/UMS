package top.dcenter.ums.security.social.banding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.social.connect.Connection;
import top.dcenter.ums.security.social.api.banding.ShowConnectionStatusViewService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.dcenter.ums.security.core.consts.SecurityConstants.CHARSET_UTF8;

/**
 * 默认的绑定状态信息回显,这里是简单实现，返回 Json格式
 * @author zyw
 * @version V1.0  Created by 2020/5/26 13:52
 */
public class DefaultShowConnectionStatusViewServiceImpl implements ShowConnectionStatusViewService {

    private final ObjectMapper objectMapper;

    public DefaultShowConnectionStatusViewServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //noinspection unchecked
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) model.get("connectionMap");

        Map<String, Boolean> result = new HashMap<>(16);
        for (Map.Entry<String, List<Connection<?>>> next : connections.entrySet())
        {
            result.put(next.getKey(), CollectionUtils.isNotEmpty(next.getValue()));
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHARSET_UTF8);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
