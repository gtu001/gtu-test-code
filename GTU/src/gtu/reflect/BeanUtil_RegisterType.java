package gtu.reflect;

import java.math.BigDecimal;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.StringConverter;

public class BeanUtil_RegisterType {

    public static void main(String[] args){
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new StringConverter(), String.class);
    }
}
