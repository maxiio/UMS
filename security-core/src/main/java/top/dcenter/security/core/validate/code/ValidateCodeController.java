package top.dcenter.security.core.validate.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import top.dcenter.security.core.enums.ValidateStatus;
import top.dcenter.security.core.excception.ValidateCodeException;
import top.dcenter.security.core.excception.ValidateCodeProcessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static top.dcenter.security.core.consts.SecurityConstants.VALIDATE_CODE_PROCESSOR_SUFFIX;


/**
 * @author zyw
 * @version V1.0  Created by 2020/5/3 23:41
 */
@RestController
@Slf4j
public class ValidateCodeController {

    private final Map<String, ValidateCodeProcessor> validateCodeProcessors;

    public ValidateCodeController(Map<String, ValidateCodeProcessor> validateCodeProcessors) {
        this.validateCodeProcessors = validateCodeProcessors;
    }


    /**
     * 获取图片验证码, 根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor} 接口实现
     * @param request request 中的 width 的值如果小于 height * 45 / 10, 则 width = height * 45 / 10
     * @param response
     * @throws IOException
     */
    @GetMapping("/code/{type}")
    public void createCode(@PathVariable("type") String type,
                                     HttpServletRequest request, HttpServletResponse response) {

        ValidateCodeProcessor validateCodeProcessor = validateCodeProcessors.get(type + VALIDATE_CODE_PROCESSOR_SUFFIX);
        if (validateCodeProcessor == null)
        {
            throw new ValidateCodeException("非法的校验码类型");
        }
        ValidateStatus validateStatus = validateCodeProcessor.produce(new ServletWebRequest(request, response));
        if (ValidateStatus.FAILURE.equals(validateStatus))
        {
            throw new ValidateCodeProcessException("获取验证码失败，请重试！");
        }

    }

}