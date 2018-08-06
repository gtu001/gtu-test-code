package gtu.springdata.jpa;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.TemplateException;
import gtu.freemarker.FreeMarkerSimpleUtil;

public class OneToManyCreaterTest {

    private static final String MASTER_BLOCK;

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                                           \n");
        sb.append("    @OneToMany(//                                                          \n");
        sb.append("            cascade = CascadeType.ALL, //                                  \n");
        sb.append("            orphanRemoval = false, //                                      \n");
        sb.append("            fetch = FetchType.LAZY//                                       \n");
        sb.append("    ) //                                                                   \n");

        switch (1) {
        case 1:// 拿pk當外建
            sb.append("    @JoinColumn(name = \"${ref_db_column}\") //                         \n");
            break;
        case 2:// 自訂外建(無效果)
            sb.append("    @JoinColumn(foreignKey = @ForeignKey(name = \"${ref_db_column}\")) //  \n");
            break;
        }

        sb.append(" @JsonIgnore \n");
        sb.append(" private List<${ref_entity_detail_type}> ${java_lst_name};                 \n");
        sb.append("                                                                           \n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        MASTER_BLOCK = sb.toString();
        sb.delete(0, sb.length());
    }

    String masterBlock;

    public void execute(Map<String, Object> root) {
        try {
            if (root.containsKey("ref_db_column") && //
                    root.containsKey("ref_entity_detail_type") && //
                    root.containsKey("java_lst_name") && //
                    true) {
                System.out.println("參數到齊!!");
            } else {
                throw new Exception("參數未到齊!!");
            }

            System.out.println("MASTER_BLOCK start-------------------------------------");
            if (true)
                masterBlock = FreeMarkerSimpleUtil.replace(MASTER_BLOCK, root);

            System.out.println(masterBlock);
        } catch (Exception e) {
            throw new RuntimeException("execute ERR : " + e.getMessage(), e);
        }
    }

    public String getMasterBlock() {
        return masterBlock;
    }

    public static void main(String[] args) throws IOException, TemplateException {
        OneToManyCreaterTest t = new OneToManyCreaterTest();
        // 請勿砍掉 ↓↓↓↓↓↓↓
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ref_db_column", "material_actual_id");// master id
        root.put("java_lst_name", "materialActualProperties"); // detail entity list
        root.put("ref_entity_detail_type", "MaterialActualProperty"); //detail entity

        t.execute(root);
        System.out.println("done...");
    }
}
