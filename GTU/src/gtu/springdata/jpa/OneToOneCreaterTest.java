package gtu.springdata.jpa;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.TemplateException;
import gtu.freemarker.FreeMarkerSimpleUtil;

public class OneToOneCreaterTest {

    private static final String MASTER_BLOCK_1;
    private static final String MASTER_BLOCK_2;
    private static final String DETAIL_BLOCK_2;

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                       \r\n");
        sb.append("    @OneToOne(//                                         \r\n");
        sb.append("            cascade = CascadeType.ALL, //                \r\n");
        sb.append("            orphanRemoval = true, //                     \r\n");
        sb.append("            fetch = FetchType.LAZY//                     \r\n");
        sb.append("    ) //                                                 \r\n");
        sb.append("    @JoinColumn(name = \"${master_db_id_fk}\")                          \r\n");
        sb.append("    @JsonIgnore                                          \r\n");
        sb.append("    private ${detail_class} ${detail_name};                                   \r\n");
        sb.append("                                                       \r\n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        MASTER_BLOCK_1 = sb.toString();
        sb.delete(0, sb.length());

        // master
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                       \r\n");
        sb.append("    @OneToOne(//                                         \r\n");
        sb.append("            mappedBy = \"${master_name}\", //                        \r\n");
        sb.append("            cascade = CascadeType.ALL, //                \r\n");
        sb.append("            orphanRemoval = true, //                     \r\n");
        sb.append("            fetch = FetchType.LAZY//                     \r\n");
        sb.append("    ) //                                                 \r\n");
        sb.append("    @JsonIgnore                                          \r\n");
        sb.append("    private ${detail_class} ${detail_name};                                   \r\n");
        sb.append("                                                       \r\n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        MASTER_BLOCK_2 = sb.toString();
        sb.delete(0, sb.length());

        // detail
        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                          \n");
        sb.append("                                                       \r\n");
        sb.append("    @OneToOne(//                                           \r\n");
        sb.append("            cascade = CascadeType.ALL, //                  \r\n");
        sb.append("            orphanRemoval = true, //                       \r\n");
        sb.append("            fetch = FetchType.LAZY//                       \r\n");
        sb.append("    ) //                                                   \r\n");
        sb.append("    @JoinColumn(name = \"${master_db_id_fk}\")                          \r\n");
        sb.append("    @JsonIgnore                                            \r\n");
        sb.append("    private ${master_class} ${master_name};                                     \r\n");
        sb.append("                                                       \r\n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        DETAIL_BLOCK_2 = sb.toString();
        sb.delete(0, sb.length());

        // master_db_id_fk = mater_db_Pk
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
        OneToOneCreaterTest t = new OneToOneCreaterTest();

        int type = 2;
        Map<String, Object> root = new HashMap<String, Object>();

        switch (type) {
        case 1:
            root.put("master_db_id_fk", "employee_id"); //
            root.put("detail_class", "Address"); //
            root.put("detail_name", "address"); //
            break;
        case 2:
            root.put("master_class", "Employee"); //
            root.put("master_name", "employee"); //
            root.put("detail_class", "Address"); //
            root.put("detail_name", "address"); //
            root.put("master_db_id_fk", "employee_id"); //
            break;
        }

        t.execute(root, type);

        System.out.println("done...");
    }

}
