package gtu.enum_;

import gtu.enum_.ItsmCrCategoryMany.BaseTransceiverStationType;
import gtu.enum_.ItsmCrCategoryMany.SystemType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * ITSM CR Category
 * 
 * @author Steve Tien
 * @version 1.0, Feb 26, 2009
 */
public enum ItsmCrCategory {

    BTS("BTS(不含Repeater and Booster)", "BTS(不含Repeater and Booster)") {

    },
    NODE_B("Node-B", "Node-B") {

    },
    REPEATER("Repeater", "Repeater") {

    },
    BOOSTER("Booster", "Booster") {

    },
    AP("AP", "AP") {

    };

    /**
     * 
     * 
     * @author Steve Tien
     * @version 1.0, Feb 26, 2009
     */
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

        /**
         * 
         * 
         * @return the btsType
         */
        public BaseTransceiverStationType getBtsType() {
            return btsType;
        }

        /**
         * 
         * 
         * @return the itsmCrCategory
         */
        public ItsmCrCategory getItsmCrCategory() {
            return itsmCrCategory;
        }

        /**
         * 
         * 
         * @return the systemType
         */
        public SystemType getSystemType() {
            return systemType;
        }

    }

    private static final String PARAMETER_CATEGORY = "ITSMCR_CATEGORY";

    private static final Map<String, ItsmCrCategory> valueToParameter = new HashMap<String, ItsmCrCategory>();

    //EnumMap TODO
    private static final Map<SystemType, Map<BaseTransceiverStationType, ItsmCrCategory>> holder = new EnumMap<SystemType, Map<BaseTransceiverStationType, ItsmCrCategory>>(
            SystemType.class);

    static {

        for (ItsmCrCategory parameter : values()) {
            valueToParameter.put(parameter.getParameterValue(), parameter);
        }

        for (SystemType systemType : SystemType.values()) {
            holder.put(systemType, new EnumMap<BaseTransceiverStationType, ItsmCrCategory>(BaseTransceiverStationType.class));
        }

        for (TypeRelation relation : TypeRelation.values()) {
            holder.get(relation.getSystemType()).put(relation.getBtsType(), relation.getItsmCrCategory());
        }

    }

    public static ItsmCrCategory from(SystemType systemType, BaseTransceiverStationType btsType) {
        return holder.get(systemType).get(btsType);
    }

    public static ItsmCrCategory fromValue(String value) {
        return valueToParameter.get(value);
    }

    private final String parameterValue;

    private final String parameterLabel;

    ItsmCrCategory(String parameterValue, String parameterLabel) {
        this.parameterValue = parameterValue;
        this.parameterLabel = parameterLabel;
    }

    public String getParameterCategory() {
        return PARAMETER_CATEGORY;
    }

    public String getParameterLabel() {
        return parameterLabel;
    }

    public String getParameterValue() {
        return parameterValue;
    }

}
