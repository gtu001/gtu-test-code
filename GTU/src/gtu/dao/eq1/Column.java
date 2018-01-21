package gtu.dao.eq1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识数据库字段的名称
 * 
 * @author 杨信
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * 字段名称
     */
    String value();

    /**
     * 字段的类型
     * 
     * @return
     */
    Class<?> type() default String.class;

    /**
     * 字段的长度
     * 
     * @return
     */
    int length() default 0;
}