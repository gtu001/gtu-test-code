package _temp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Test19_SqlUnionAll {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Map<String, String>> set1 = new ArrayList<Map<String, String>>();
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "廉慧忠").put("person_id", "P129428012").put("birth_date", "0540911").put("education_mark", "22").put("school_code","999999").put("site_id", "65000030").build());
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "廉怡婷").put("person_id", "P223183730").put("birth_date", "0770505").put("education_mark", "21").put("school_code","999999").put("site_id", "65000030").build());
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "周威任").put("person_id", "X108392102").put("birth_date", "0400601").put("education_mark", "12").put("school_code","999999").put("site_id", "65000120").build());
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "何柏翔").put("person_id", "X108428474").put("birth_date", "0400801").put("education_mark", "72").put("school_code","999999").put("site_id", "65000120").build());
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "錢昭亮").put("person_id", "X108437973").put("birth_date", "0281003").put("education_mark", "82").put("school_code","999999").put("site_id", "65000120").build());
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "呂博文").put("person_id", "X108461353").put("birth_date", "0231226").put("education_mark", "82").put("school_code","999999").put("site_id", "65000120").build());
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "陶文馨").put("person_id", "X108526475").put("birth_date", "0420625").put("education_mark", "22").put("school_code","999999").put("site_id", "65000120").build());
        set1.add(ImmutableMap.<String,String>builder().put("person_name", "韓怡旺").put("person_id", "X108591310").put("birth_date", "0380303").put("education_mark", "62").put("school_code","999999").put("site_id", "65000120").build());

        StringBuilder sb = new StringBuilder();
        sb.append("                                                                                             \n");
        sb.append(" select                                                                                      \n");
        sb.append(" a.person_id,                                                                                \n");
        sb.append(" a.education_mark as rlm, a.education_code as rlc,                                           \n");
        sb.append(" b.education_mark as c1m, b.education_code as c1mc,                                          \n");
        sb.append(" c.education_mark as c4m, c.education_code as c4c,                                           \n");
        sb.append(" d.education_mark as rrm, d.education_code as rrc                                            \n");
        sb.append(" from                                                                                        \n");
        sb.append(" teun0020:rldf004m a left join                                                               \n");
        sb.append(" teun0000:rrdf004m b on a.person_id = b.person_id and a.site_id = b.site_id left join        \n");
        sb.append(" chun0000:rrdf004m c on a.person_id = c.person_id and a.site_id = c.site_id left join        \n");
        sb.append(" chun0000:rcdf001m d on a.person_id = d.person_id and a.site_id = d.site_id                  \n");
        sb.append(" where                                                                                       \n");
        sb.append(" a.person_id = ''{0}'' and a.site_id = ''{1}''                                       \n");
        sb.append(" union all                                                                                   \n");
        sb.append("                                                                                             \n");
        final String SQL = sb.toString();
        
        StringBuffer sb2 = new StringBuffer();
        for(Map<String, String> valMap : set1){
            sb2.append(MessageFormat.format(SQL, new Object[]{valMap.get("person_id"), valMap.get("site_id")}));
        }
        
        System.out.println(sb2);
    }

}
