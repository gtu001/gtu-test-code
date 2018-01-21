package gtu.spring.steve;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * ITSM IS_GT_6HR Procedure
 * 
 * @author Steve Tien
 * @version 1.0, Mar 26, 2009
 */
public class ItsmCrIsUrgentCrRequiredProcedure extends StoredProcedure {

	public static void main(String[] args) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		Date planStartDate = new Date();
		boolean isUrgentCrRequired = ItsmCrIsUrgentCrRequiredProcedure
				.builder(jdbcTemplate).planStartDate(planStartDate).build()
				.check();
		System.out.println("isUrgentCrRequired = " + isUrgentCrRequired);
	}

	/**
	 * 
	 * 
	 * @author Steve Tien
	 * @version 1.0, Mar 26, 2009
	 */
	public static class Builder {
		// Required Parameter
		private final JdbcTemplate jdbcTemplate;

		// Optional Parameter
		private String procedureName = "ITSM_IS_GT_6HR";
		private Date planStartDate = new Date();
		private String planDateFormat = "%1$tY/%1$tm/%1$td %1$tH:%1$tM";
		private String urgentRequired = "N";
		private String urgentOptional = "Y";

		// 建構class
		public ItsmCrIsUrgentCrRequiredProcedure build() {
			return new ItsmCrIsUrgentCrRequiredProcedure(this);
		}

		// Required Parameter Setter
		private Builder(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}

		// Optional Parameter Setter
		public Builder planDateFormat(String planDateFormat) {
			this.planDateFormat = planDateFormat;
			return this;
		}

		public Builder planStartDate(Date planStartDate) {
			this.planStartDate = new Date(planStartDate.getTime());
			return this;
		}

		public Builder procedureName(String procedureName) {
			this.procedureName = procedureName;
			return this;
		}

		public Builder urgentOptional(String urgentOptional) {
			this.urgentOptional = urgentOptional;
			return this;
		}

		public Builder urgentRequired(String urgentRequired) {
			this.urgentRequired = urgentRequired;
			return this;
		}
	}

	private static final String RESULT = "Result";
	private static final String PLAN_START_DATE = "PlanStartDate";
	private static final String BLANK_DATE = "BlankDate";

	public static Builder builder(JdbcTemplate jdbcTemplate) {
		return new Builder(jdbcTemplate);
	}

	private final Date planStartDate;
	private final String planDateFormat;
	private final String urgentRequired;
	private final String urgentOptional;

	public ItsmCrIsUrgentCrRequiredProcedure(Builder builder) {
		this.planStartDate = new Date(builder.planStartDate.getTime());
		this.planDateFormat = builder.planDateFormat;
		this.urgentRequired = builder.urgentRequired;
		this.urgentOptional = builder.urgentOptional;

		setJdbcTemplate(builder.jdbcTemplate);
		setSql(builder.procedureName);

		setFunction(false);

		// spring
		declareParameter(new SqlParameter(BLANK_DATE, Types.VARCHAR));
		declareParameter(new SqlParameter(PLAN_START_DATE, Types.VARCHAR));
		declareParameter(new SqlOutParameter(RESULT, Types.VARCHAR));

		compile();
	}

	public boolean check() {
		boolean isUrgentCrRequired = false;
		Map<String, String> resultMap = execute();
		String result = StringUtils.defaultString(resultMap.get(RESULT),
				urgentOptional);
		if (result.equalsIgnoreCase(urgentRequired)) {
			isUrgentCrRequired = true;
		} else {
			isUrgentCrRequired = false;
		}
		return isUrgentCrRequired;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> execute() {

		String crPlanStartDateParameter = String.format(planDateFormat,
				planStartDate);

		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(BLANK_DATE, "");
		parameterMap.put(PLAN_START_DATE, crPlanStartDateParameter);
		parameterMap.put(RESULT, "");

		//執行父類別的StoredProcedure execute()
		Map result = execute(parameterMap);

		return result;
	}
}
