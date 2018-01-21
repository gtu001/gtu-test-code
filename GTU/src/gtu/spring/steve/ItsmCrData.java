package gtu.spring.steve;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.jdbc.core.JdbcTemplate;


public class ItsmCrData {

	/**
	 * @author Steve Tien
	 * @version 1.0, Mar 27, 2009
	 */
	private static enum IsUrgentCrRequiredCheckLevel {

		CALENDAR_DAY() {
			@Override
			public boolean check(Date planStartDate, JdbcTemplate jdbcTemplate) {
				boolean isUrgentCrRequired = false;
				planStartDate = new Date(planStartDate.getTime());
				Calendar calendar = new GregorianCalendar();
				Date currentDate = calendar.getTime();
				calendar.add(Calendar.HOUR_OF_DAY, 6);
				Date sixHourAfterCurrentDate = calendar.getTime();
				if (planStartDate.before(sixHourAfterCurrentDate)) {
					isUrgentCrRequired = true;
				} else {
					isUrgentCrRequired = false;
				}
				return isUrgentCrRequired;
			}

		},

		WORKING_HOUR() {
			@Override
			public boolean check(Date planStartDate, JdbcTemplate jdbcTemplate) {
				boolean isUrgentCrRequired = false;
				try {
					isUrgentCrRequired = ItsmCrIsUrgentCrRequiredProcedure.builder(jdbcTemplate)
						.planStartDate(planStartDate).build().check();
				} catch (Exception ex) {
					isUrgentCrRequired = CALENDAR_DAY.check(planStartDate,
							jdbcTemplate);
				}
				return isUrgentCrRequired;
			}
		};

		public abstract boolean check(Date planStartDate,
				JdbcTemplate jdbcTemplate);

	}
}
