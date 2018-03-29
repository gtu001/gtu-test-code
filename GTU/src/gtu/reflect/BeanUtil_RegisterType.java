package gtu.reflect;

import java.math.BigDecimal;

import org.apache.commons.beanutils.ConvertUtils;
//import org.apache.commons.beanutils.converters.BigDecimalConverter;
//import org.apache.commons.beanutils.converters.StringConverter;

public class BeanUtil_RegisterType {

    public static void main(String[] args) {
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new StringConverter(), String.class);

        // 注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
        ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
        ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
    }
}
