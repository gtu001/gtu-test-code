package gtu.spring.jdbc;

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
public class StoredProcedureTest extends StoredProcedure {

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        new StoredProcedureTest(jdbcTemplate).doExecute();
    }

    private final Date planStartDate = new Date();
    private final String urgentRequired = "N";
    private final String urgentOptional = "Y";

    public StoredProcedureTest(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
        setSql("CL.ITSM_IS_GT_6HR");

        setFunction(false);

        // 定義變數
        declareParameter(new SqlParameter("BlankDate", Types.VARCHAR));
        declareParameter(new SqlParameter("PlanStartDate", Types.VARCHAR));
        declareParameter(new SqlOutParameter("Result", Types.VARCHAR));

        compile();
    }

    /**
     * 
     * 
     * @return
     */
    public boolean doExecute() {
        boolean isUrgentCrRequired = false;

        String crPlanStartDateParameter = String.format("%1$tY/%1$tm/%1$td %1$tH:%1$tM", planStartDate);
        Map<String, String> parameterMap = new HashMap<String, String>();

        // 塞參數
        parameterMap.put("BlankDate", "");
        parameterMap.put("PlanStartDate", crPlanStartDateParameter);
        parameterMap.put("Result", "");

        Map<String, Object> resultMap = execute(parameterMap);

        // 取得out parameter
        String result = StringUtils.defaultString((String) resultMap.get("Result"), urgentOptional);
        if (result.equalsIgnoreCase(urgentRequired)) {
            isUrgentCrRequired = true;
        } else {
            isUrgentCrRequired = false;
        }
        return isUrgentCrRequired;
    }
}
