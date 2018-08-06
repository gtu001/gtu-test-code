package gtu.springdata.jpa;

import java.util.HashMap;
import java.util.Map;

import gtu.freemarker.FreeMarkerSimpleUtil;

/**
 * https://openhome.cc/Gossip/EJB3Gossip/ManyToMany.html
 * 
 * @author wistronits
 */
public class ManyToManyCreaterTest {

    private static final String FROM_ENTITY_APPENDER;
    private static final String TARGET_ENTITY_APPENDER;

    static {
        StringBuffer sb = new StringBuffer();
        // from entity
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                                                       \r\n");
        sb.append("    @ManyToMany(cascade=CascadeType.ALL)                                               \r\n");
        sb.append("    @JoinTable(                                                                        \r\n");
        sb.append("        name=\"${middle_table}\", //                                                   \r\n");
        sb.append("        joinColumns={@JoinColumn(name=\"${middle_table_from_column}\")}, //              \r\n");
        sb.append("        inverseJoinColumns={@JoinColumn(name=\"${middle_table_target_column}\")} //     \r\n");
        sb.append("    )                                                                                  \r\n");
        sb.append("    private Set<${target_entity}> ${target_entity_list}; //                                 \r\n");
        sb.append("                                                                                       \r\n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        FROM_ENTITY_APPENDER = sb.toString();
        sb.delete(0, sb.length());

        // target entity
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                                                       \r\n");
        sb.append("    @ManyToMany(cascade=CascadeType.ALL, mappedBy=\"${target_entity_list}\") //          \r\n");
        sb.append("    private Set<${from_entity}> ${from_entity_list};                                       \r\n");
        sb.append("                                                                                       \r\n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        TARGET_ENTITY_APPENDER = sb.toString();
        sb.delete(0, sb.length());
    }

    private String fromEntityBlock;
    private String targetEntityBlock;

    public void execute(Map<String, Object> root) {
        try {
            if (root.containsKey("middle_table") && //
                    root.containsKey("middle_table_from_column") && //
                    root.containsKey("middle_table_target_column") && //
                    root.containsKey("target_entity") && //
                    root.containsKey("target_entity_list") && //
                    root.containsKey("from_entity") && //
                    root.containsKey("from_entity_list") && //
                    true) {
                System.out.println("參數到齊!!");
            } else {
                throw new Exception("參數未到齊!!");
            }

            System.out.println("MASTER_BLOCK start-------------------------------------");
            fromEntityBlock = FreeMarkerSimpleUtil.replace(FROM_ENTITY_APPENDER, root);
            System.out.println("DETAIL_BLOCK start-------------------------------------");
            targetEntityBlock = FreeMarkerSimpleUtil.replace(TARGET_ENTITY_APPENDER, root);

            System.out.println(fromEntityBlock);
            System.out.println(targetEntityBlock);
        } catch (Exception e) {
            throw new RuntimeException("execute ERR : " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        ManyToManyCreaterTest t = new ManyToManyCreaterTest();

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("middle_table", "T_USER_SERVER");
        root.put("middle_table_from_column", "USER_FK");
        root.put("middle_table_target_column", "SERVER_FK");
        root.put("target_entity", "Server");
        root.put("target_entity_list", "servers");
        root.put("from_entity", "User");
        root.put("from_entity_list", "users");

        t.execute(root);

        System.out.println("done...");
    }

    public String getFromEntityBlock() {
        return fromEntityBlock;
    }

    public String getTargetEntityBlock() {
        return targetEntityBlock;
    }
}
