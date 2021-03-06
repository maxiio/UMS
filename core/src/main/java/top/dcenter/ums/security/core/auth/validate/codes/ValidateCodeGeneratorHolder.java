package top.dcenter.ums.security.core.auth.validate.codes;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import top.dcenter.ums.security.core.api.validate.code.ValidateCodeGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码生产器 holder
 * @author zyw
 * @version V1.0  Created by 2020/9/22 19:35
 */
public class ValidateCodeGeneratorHolder implements InitializingBean {

    /**
     * Map&#60;type, ValidateCodeGenerator&#62;
     */
    private Map<String, ValidateCodeGenerator<?>> validateCodeGenerators;

    @Autowired
    private GenericApplicationContext applicationContext;


    /**
     * 根据 type 获取 {@link ValidateCodeGenerator}，如果不存在则返回 null
     * @param type  验证码类型
     * @return  如果 {@link ValidateCodeGenerator} 不存在则返回 null
     */
    public ValidateCodeGenerator<?> findValidateCodeGenerator(ValidateCodeType type) {
        if (type == null)
        {
            return null;
        }
        ValidateCodeGenerator<?> validateCodeGenerator;
        try {
            validateCodeGenerator = this.validateCodeGenerators.get(type.name().toLowerCase());
        }
        catch (Exception e) {
            validateCodeGenerator = null;
        }
        return validateCodeGenerator;
    }

    /**
     * 根据 type 获取 {@link ValidateCodeGenerator} 如果不存在则返回 null
     * @param type  验证码类型
     * @return  如果 {@link ValidateCodeGenerator} 不存在则返回 null
     */
    public ValidateCodeGenerator<?> findValidateCodeProcessor(String type) {
        if (type == null)
        {
            return null;
        }
        return this.validateCodeGenerators.get(type);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void afterPropertiesSet() {

        // 解决循环应用问题
        Map<String, ValidateCodeGenerator> validateCodeGeneratorMap = applicationContext.getBeansOfType(ValidateCodeGenerator.class);
        Collection<ValidateCodeGenerator> values = validateCodeGeneratorMap.values();
        validateCodeGenerators = new HashMap<>(values.size());
        for (ValidateCodeGenerator value : values)
        {
            validateCodeGenerators.put(value.getValidateCodeType().toLowerCase(), value);
        }

    }
}
