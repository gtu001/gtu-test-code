package gtu.enum_;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Steve Tien
 * @version 1.0, Mar 5, 2009
 */
public enum ItsmCrType {

	//常數宣告
	NORMAL("N", "一般異動") {
	},
	URGENT("E", "緊急異動") {
	};

	private static final String PARAMETER_CATEGORY = "ITSMCR_CR_TYPE";
	
	//Map 用於以值反向取得常數名稱
	private static final Map<String, ItsmCrType> valueToParameter = new HashMap<String, ItsmCrType>();
	static {
		for (ItsmCrType parameter : values()) {
			valueToParameter.put(parameter.getParameterValue(), parameter);
		}
	}

	
	public static ItsmCrType fromValue(String value) {
		return valueToParameter.get(value);
	}

	public static String getParameterCategory() {
		return PARAMETER_CATEGORY;
	}

	private final String parameterValue;
	private final String parameterLabel;

	ItsmCrType(String parameterValue, String parameterLabel) {
		this.parameterValue = parameterValue;
		this.parameterLabel = parameterLabel;
	}

	public String getParameterLabel() {
		return parameterLabel;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public static void main(String[] args) {
		System.out.println(ItsmCrType.fromValue("N"));
		System.out.println(ItsmCrType.fromValue("E"));
	}
}
