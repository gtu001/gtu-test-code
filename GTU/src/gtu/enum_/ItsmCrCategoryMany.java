package gtu.enum_;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


/**
 * ITSM CR Category
 * 
 * @author Steve Tien
 * @version 1.0, Feb 26, 2009
 */
public class ItsmCrCategoryMany {
	
    public static enum TypeRelation {

        GSM900_MACRO(SystemType.GSM900, BaseTransceiverStationType.MACRO, ItsmCrCategory.BTS),
        GSM900_MICRO(SystemType.GSM900, BaseTransceiverStationType.MICRO, ItsmCrCategory.BTS),
        GSM900_MAXITE(SystemType.GSM900, BaseTransceiverStationType.MAXITE, ItsmCrCategory.BTS),
        GSM900_REPEATER(SystemType.GSM900, BaseTransceiverStationType.REPEATER, ItsmCrCategory.REPEATER),
        GSM900_BOOSTER(SystemType.GSM900, BaseTransceiverStationType.BOOSTER, ItsmCrCategory.BOOSTER),
        GSM900_HOME_BOOSTER(SystemType.GSM900, BaseTransceiverStationType.HOME_BOOSTER, ItsmCrCategory.BOOSTER),
        GSM900_PICO_HOME_BOOSTER(SystemType.GSM900, BaseTransceiverStationType.PICO_HOME_BOOSTER, ItsmCrCategory.BOOSTER),

        DCS1800_MACRO(SystemType.DCS1800, BaseTransceiverStationType.MACRO, ItsmCrCategory.BTS),
        DCS1800_MICRO(SystemType.DCS1800, BaseTransceiverStationType.MICRO, ItsmCrCategory.BTS),
        DCS1800_MAXITE(SystemType.DCS1800, BaseTransceiverStationType.MAXITE, ItsmCrCategory.BTS),
        DCS1800_REPEATER(SystemType.DCS1800, BaseTransceiverStationType.REPEATER, ItsmCrCategory.REPEATER),
        DCS1800_BOOSTER(SystemType.DCS1800, BaseTransceiverStationType.BOOSTER, ItsmCrCategory.BOOSTER),
        DCS1800_HOME_BOOSTER(SystemType.DCS1800, BaseTransceiverStationType.HOME_BOOSTER, ItsmCrCategory.BOOSTER),
        DCS1800_PICO_HOME_BOOSTER(SystemType.DCS1800, BaseTransceiverStationType.PICO_HOME_BOOSTER, ItsmCrCategory.BOOSTER),

        WCDMA_MACRO(SystemType.WCDMA, BaseTransceiverStationType.MACRO, ItsmCrCategory.NODE_B),
        WCDMA_MICRO(SystemType.WCDMA, BaseTransceiverStationType.MICRO, ItsmCrCategory.NODE_B),
        WCDMA_MAXITE(SystemType.WCDMA, BaseTransceiverStationType.MAXITE, ItsmCrCategory.NODE_B),
        WCDMA_REPEATER(SystemType.WCDMA, BaseTransceiverStationType.REPEATER, ItsmCrCategory.REPEATER),
        WCDMA_BOOSTER(SystemType.WCDMA, BaseTransceiverStationType.BOOSTER, ItsmCrCategory.BOOSTER),
        WCDMA_HOME_BOOSTER(SystemType.WCDMA, BaseTransceiverStationType.HOME_BOOSTER, ItsmCrCategory.BOOSTER),
        WCDMA_PICO_HOME_BOOSTER(SystemType.WCDMA, BaseTransceiverStationType.PICO_HOME_BOOSTER, ItsmCrCategory.BOOSTER),

        WIMAX_MACRO(SystemType.WIMAX, BaseTransceiverStationType.MACRO, ItsmCrCategory.AP),
        WIMAX_MICRO(SystemType.WIMAX, BaseTransceiverStationType.MICRO, ItsmCrCategory.AP),
        WIMAX_MAXITE(SystemType.WIMAX, BaseTransceiverStationType.MAXITE, ItsmCrCategory.AP),
        WIMAX_REPEATER(SystemType.WIMAX, BaseTransceiverStationType.REPEATER, ItsmCrCategory.AP),
        WIMAX_BOOSTER(SystemType.WIMAX, BaseTransceiverStationType.BOOSTER, ItsmCrCategory.AP),
        WIMAX_HOME_BOOSTER(SystemType.WIMAX, BaseTransceiverStationType.HOME_BOOSTER, ItsmCrCategory.AP),
        WIMAX_PICO_HOME_BOOSTER(SystemType.WIMAX, BaseTransceiverStationType.PICO_HOME_BOOSTER, ItsmCrCategory.AP);
        
        private final SystemType systemType;
        private final BaseTransceiverStationType btsType;
        private final ItsmCrCategory itsmCrCategory;
        
        TypeRelation(SystemType systemType, BaseTransceiverStationType btsType, ItsmCrCategory itsmCrCategory) {
            this.systemType = systemType;
            this.btsType = btsType;
            this.itsmCrCategory = itsmCrCategory;
        }
        public BaseTransceiverStationType getBtsType() {
            return btsType;
        }
        public ItsmCrCategory getItsmCrCategory() {
            return itsmCrCategory;
        }
        public SystemType getSystemType() {
            return systemType;
        }
    }
    

    public static enum ItsmCrCategory{
	    BTS("BTS(不含Repeater and Booster)", "BTS(不含Repeater and Booster)"),
	    NODE_B("Node-B", "Node-B"),
	    REPEATER("Repeater", "Repeater"),
	    BOOSTER("Booster", "Booster"),
	    AP("AP", "AP"),
	    UNDEFINED("", "");
	    private final String parameterValue;
	    private final String parameterLabel;
	    ItsmCrCategory(String parameterValue, String parameterLabel) {
	        this.parameterValue = parameterValue;
	        this.parameterLabel = parameterLabel;
	    }
	    private static final Map<String, ItsmCrCategory> valueToParameter = new HashMap<String, ItsmCrCategory>();
	    static {
	        for (ItsmCrCategory type : values()) {
	            valueToParameter.put(type.getParameterValue(), type);
	        }
	    }
	    public static ItsmCrCategory fromValue(String value) {
	        if (valueToParameter.containsKey(value)) {
	            return valueToParameter.get(value);
	        } else {
	            return UNDEFINED;
	        }
	    }
	    public String getParameterLabel() {
	        return parameterLabel;
	    }
	    public String getParameterValue() {
	        return parameterValue;
	    }
	}
    
    public static enum SystemType {
        DCS1800("DCS", "DCS1800"),
        GSM900("GSM", "GSM900"),
        WCDMA("3G", "WCDMA"),
        WIMAX("WIMAX", "WiMAX"),
        UNDEFINED("", "");
        private final String parameterValue;
        private final String parameterLabel;
        SystemType(String parameterValue, String parameterLabel) {
            this.parameterValue = parameterValue;
            this.parameterLabel = parameterLabel;
        }
        private static final Map<String, SystemType> valueToParameter = new HashMap<String, SystemType>();
        static {
            for (SystemType type : values()) {
                valueToParameter.put(type.getParameterValue(), type);
            }
        }
        public static SystemType fromValue(String value) {
            if (valueToParameter.containsKey(value)) {
                return valueToParameter.get(value);
            } else {
                return UNDEFINED;
            }
        }
        public String getParameterLabel() {
            return parameterLabel;
        }
        public String getParameterValue() {
            return parameterValue;
        }
    }
    
    public static enum BaseTransceiverStationType {
        MACRO("A1", "Macro"),
        MICRO("A2", "Micro"),
        MAXITE("A3", "Maxite"),
        REPEATER("B1", "Repeater"),
        BOOSTER("B2", "Booster"),
        HOME_BOOSTER("B3", "Home Booster"),
        PICO_HOME_BOOSTER("B4", "Pico Home Booster"),
        UNDEFINED("", "");
        private final String parameterValue;
        private final String parameterLabel;
        BaseTransceiverStationType(String parameterValue, String parameterLabel) {
            this.parameterValue = parameterValue;
            this.parameterLabel = parameterLabel;
        }
        private static final Map<String, BaseTransceiverStationType> valueToParameter = new HashMap<String, BaseTransceiverStationType>();
        static {
            for (BaseTransceiverStationType type : values()) {
                valueToParameter.put(type.getParameterValue(), type);
            }
        }
        public static BaseTransceiverStationType fromValue(String value) {
            if (valueToParameter.containsKey(value)) {
                return valueToParameter.get(value);
            } else {
                return UNDEFINED;
            }
        }
        public String getParameterLabel() {
            return parameterLabel;
        }
        public String getParameterValue() {
            return parameterValue;
        }
    }

    
    public static void main(String[] args){
    	for(TypeRelation typeRelation : TypeRelation.values()){
    		System.out.println(typeRelation.name()+".."+typeRelation.ordinal());
    		System.out.println("SystemType:"+typeRelation.getSystemType());
    		System.out.println("BtsType:"+typeRelation.getBtsType());
    		System.out.println("ItsmCrCategory:"+typeRelation.getItsmCrCategory());
    		System.out.println("==============");
    	}
    }
}
