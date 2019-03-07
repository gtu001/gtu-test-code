package gtu.reflect;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.apache.commons.lang3.reflect.FieldUtils;

public class BeanEnumCopyUtil {

    public interface BeanPropertiesCopyInterface {
        public String getTheir();

        public String getSelf();
    }

    public static void copyProperties(Object self, Object their, BeanPropertiesCopyInterface[] values) {
        Field theirField = null;
        Field selfField = null;
        for (BeanPropertiesCopyInterface e : values) {
            try {
                theirField = FieldUtils.getDeclaredField(their.getClass(), e.getTheir(), true);
                selfField = FieldUtils.getDeclaredField(self.getClass(), e.getSelf(), true);
                Object val = FieldUtils.readDeclaredField(their, e.getTheir(), true);
                if (theirField.getType() != selfField.getType()) {
                    if (selfField.getType() == BigDecimal.class) {
                        val = new BigDecimal((String) val);
                    }
                    // TODO 新增型別在此
                }
                FieldUtils.writeDeclaredField(self, e.getSelf(), val, true);
            } catch (Exception e1) {
                throw new RuntimeException(String.format("轉型錯誤  , self.%s (%s) <- their.%s (%s)",
                    selfField.getName(), selfField.getType().getSimpleName(), theirField.getName(),
                    theirField.getType().getSimpleName()));
            }
        }
    }
}
