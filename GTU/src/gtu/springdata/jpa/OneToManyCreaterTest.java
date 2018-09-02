package gtu.springdata.jpa;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.TemplateException;
import gtu.freemarker.FreeMarkerSimpleUtil;

public class OneToManyCreaterTest {

    private static final String MASTER_BLOCK_1;
    private static final String MASTER_BLOCK_2;
    private static final String DETAIL_BLOCK_2;

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
        MASTER_BLOCK_1 = sb.toString();
        sb.delete(0, sb.length());

        // master
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                       \r\n");
        sb.append("    @OneToMany(//                                      \r\n");
        sb.append("            mappedBy = \"${java_master_name}\", //                  \r\n");
        sb.append("            cascade = CascadeType.ALL, //              \r\n");
        sb.append("            orphanRemoval = true//                     \r\n");
        sb.append("    )                                                  \r\n");
        sb.append("    private List<${ref_entity_detail_type}> ${java_lst_name};                            \r\n");
        sb.append("                                                       \r\n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        MASTER_BLOCK_2 = sb.toString();
        sb.delete(0, sb.length());

        // detail
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                       \r\n");
        sb.append("    @ManyToOne(fetch = FetchType.LAZY)                 \r\n");
        sb.append("    @JoinColumn(name = \"${ref_db_column}\")                  \r\n");
        sb.append("    private ${ref_entity_master_type} ${java_master_name};                         \r\n");
        sb.append("                                                       \r\n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        DETAIL_BLOCK_2 = sb.toString();
        sb.delete(0, sb.length());
    }

    String masterBlock;
    String detailBlock;

    public void execute(Map<String, Object> root, int type) {
        try {
            switch (type) {
            case 1:
                System.out.println("<1>MASTER_BLOCK start-------------------------------------");
                masterBlock = FreeMarkerSimpleUtil.replace(MASTER_BLOCK_1, root);
                System.out.println(masterBlock);
                break;
            case 2:
                System.out.println("<2>MASTER_BLOCK start-------------------------------------");
                masterBlock = FreeMarkerSimpleUtil.replace(MASTER_BLOCK_2, root);
                System.out.println(masterBlock);
                System.out.println("<2>DETAIL_BLOCK start-------------------------------------");
                detailBlock = FreeMarkerSimpleUtil.replace(DETAIL_BLOCK_2, root);
                System.out.println(detailBlock);
                break;
            default:
                throw new Exception("未知type : " + type);
            }

        } catch (Exception e) {
            throw new RuntimeException("execute ERR : " + e.getMessage(), e);
        }
    }

    public String getMasterBlock() {
        return masterBlock;
    }

    public String getDetailBlock() {
        return detailBlock;
    }

    public static void main(String[] args) throws IOException, TemplateException {
        OneToManyCreaterTest t = new OneToManyCreaterTest();

        int type = 2;
        Map<String, Object> root = new HashMap<String, Object>();

        switch (type) {
        case 1:
            root.put("ref_db_column", "material_actual_id");// mater db pk
                                                            // (forign key)
            root.put("java_lst_name", "materialActualProperties"); // detail
                                                                   // uncap lst
                                                                   // name
            root.put("ref_entity_detail_type", "MaterialActualProperty"); // detail
                                                                          // class
                                                                          // name

            break;
        case 2:
            root.put("java_master_name", "employee"); // master entity uncap
                                                      // name
            root.put("ref_entity_detail_type", "Car"); // detail class name
            root.put("java_lst_name", "cars"); // detail uncap lst name
            root.put("ref_db_column", "employee_id"); // mater db pk (forign
                                                      // key)
            root.put("ref_entity_master_type", "Empolyee");// master class name
            break;
        }

        t.execute(root, type);

        System.out.println("done...");
    }

}
