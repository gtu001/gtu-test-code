package gtu.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

public class QueryForMap {

    public static void main(String[] args) {
        Session session = null;
        SQLQuery sqlQuery = session.createSQLQuery(//
                "select r1.person_name, r2m.modify_yyymmdd, r2m.original_name, r2m.admin_office_code " + // XXX
                        " FROM RCDF002M r2m inner join rcdfp001 r1 on r1.person_id=r2m.person_id " + //
                        " where r2m.person_id = ? ");
        sqlQuery.setString(0, "person_id");
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> queryList1 = sqlQuery.list();
    }

}
