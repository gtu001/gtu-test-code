package gtu.enum_;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class SteveEnumTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	IsErpSerialControl is = IsErpSerialControl.fromRecordValue("5");
        System.out.println(is.toBooleanValue());
        System.out.println(is.toRecordValue());
    }
    
    private static enum IsErpSerialControl {
        N("1", false) {
        },
        Y("5", true) {
        },
        ;

        private static final Map<String, IsErpSerialControl> fromRecordValue;
        
        static {
            final ImmutableMap.Builder<String, IsErpSerialControl> builder = ImmutableMap.builder();
            for (IsErpSerialControl enumValue : values()) {
                builder.put(enumValue.toRecordValue(), enumValue);
            }
            fromRecordValue = builder.build();
        }
        
        public static IsErpSerialControl fromRecordValue(String recordValue) {
            if (fromRecordValue.containsKey(recordValue)) {
                return fromRecordValue.get(recordValue);
            } else {
                return Y;
            }
        }

        private final String recordValue;
        private final boolean booleanValue;

        private IsErpSerialControl(final String recordValue, final boolean booleanValue) {
            this.recordValue = recordValue;
            this.booleanValue = booleanValue;
        }

        public String toRecordValue() {
            return recordValue;
        }
        public boolean toBooleanValue() {
            return booleanValue;
        }
    }
}
