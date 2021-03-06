package top.dcenter.ums.security.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import top.dcenter.ums.security.core.auth.filter.AjaxOrFormRequestFilter;
import top.dcenter.ums.security.core.consts.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static top.dcenter.ums.security.core.consts.SecurityConstants.URL_PARAMETER_SEPARATOR;

/**
 * request 工具
 * @author zyw
 * @version V1.0  Created by 2020/5/30 16:19
 */
public class RequestUtil {

    /**
     * 验证 request 中参数是否是 json 的字符串的前缀,
     */
    public static final String VALIDATE_JSON_PREFIX  = AjaxOrFormRequestFilter.VALIDATE_JSON_PREFIX;

    /**
     * 提取 request 中的 json 数据. 转换为 T 对象
     * @param request   json类型的输入流, not Null
     * @param objectMapper  jackson 实列, not Null
     * @param clz  Class<T>, not Null
     * @return T, 当出现错误或没有元素时返回一个 null
     */
    public static <T> T extractRequest2Object(HttpServletRequest request, ObjectMapper objectMapper,
                                                            Class<T> clz) {
        try
        {
            byte[] bodies;
            if (request instanceof AjaxOrFormRequestFilter.AjaxOrFormRequest)
            {
                AjaxOrFormRequestFilter.AjaxOrFormRequest ajaxOrFormRequest = (AjaxOrFormRequestFilter.AjaxOrFormRequest) request;
                bodies = ajaxOrFormRequest.getBody();
            }
            else
            {
                bodies = request.getInputStream().readAllBytes();
            }

            String requestBody = new String(bodies, StandardCharsets.UTF_8).trim();
            if (StringUtils.isBlank(requestBody))
            {
                return null;
            }

            return requestBody2Object(objectMapper, clz, requestBody);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 根据提供的 clz 与 requestBody 转换为 clz 实例
     * @param objectMapper  objectMapper
     * @param clz   Class
     * @param requestBody   json 字符串 或 FormData 字符串 或 queryString
     * @return T 返回 clz 的实例
     * @throws com.fasterxml.jackson.core.JsonProcessingException   JsonProcessingException
     */
    public static <T> T requestBody2Object(ObjectMapper objectMapper, Class<T> clz, String requestBody) throws com.fasterxml.jackson.core.JsonProcessingException {
        if (StringUtils.isBlank(requestBody))
        {
            return null;
        }

        if (StringUtils.startsWith(requestBody, VALIDATE_JSON_PREFIX))
        {
            return objectMapper.readValue(requestBody, clz);
        }
        else
        {
            Map<String, Object> map = ConvertUtil.string2JsonMap(requestBody, URL_PARAMETER_SEPARATOR,
                                             SecurityConstants.KEY_VALUE_SEPARATOR);
            return objectMapper.readValue(map.toString(), clz);
        }
    }

}

