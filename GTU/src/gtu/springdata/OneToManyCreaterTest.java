package gtu.springdata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.TemplateException;
import gtu.freemarker.FreeMarkerSimpleUtil;

public class OneToManyCreaterTest {

    private static final String MASTER_BLOCK;
    private static final String DETAIL_BLOCK;
    private static final String REPOSITORY_BLOCK;

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
            sb.append("    @JoinColumn(name = \"${ref_db_column!'ref_db_column'}\") //                         \n");
            break;
        case 2:// 自訂外建(無效果)
            sb.append("    @JoinColumn(foreignKey = @ForeignKey(name = \"${ref_db_column!'ref_db_column'}\")) //  \n");
            break;
        }
        
        sb.append("    @JsonIgnore                                                            \n");
        sb.append("    private List<${ref_java_type!'ref_java_type'}> ${java_lst_name!'java_lst_name'};         \n");
        sb.append("                                                                           \n");
        sb.append("    @Transient                                                             \n");
        sb.append("    @DynamicDBRelation(//                                                  \n");
        sb.append("            setter = \"${setter!'setter'}\", //                            \n");
        sb.append("            repository = \"${repository!'repository'}\", //                  \n");
        sb.append("            method = \"${method!'method'}\")                                \n");
        sb.append("    private String ${ref_java_name!'ref_java_name'};                                  \n");
        sb.append("                                                                           \n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                          \n");
        MASTER_BLOCK = sb.toString();
        sb.delete(0, sb.length());

        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                                          \n");
        sb.append("                                                                                                                                                       \n");
        sb.append("     @Query(value = \"select t from ${ref_java_type!'ref_java_type'} t where t.${ref_java_name!'ref_java_name'} = :param1 \", nativeQuery = false)       \n");
        sb.append("     public List<${ref_java_type!'ref_java_type'}> ${method!'method'}(@Param(\"param1\") Long param1);        \n");
        sb.append("                                                                                                                                                       \n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                                          \n");

        REPOSITORY_BLOCK = sb.toString();
        sb.delete(0, sb.length());

        sb.append("    // relation ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓                 \n");
        sb.append("    @Column(name = \"${ref_db_column!'ref_db_column'}\")       \n");
        sb.append("    private Long ${ref_java_name};              \n");
        sb.append("    // relation ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑                 \n");
        sb.append("                                                                  \n");

        DETAIL_BLOCK = sb.toString();
        sb.delete(0, sb.length());
    }

    String masterBlock;
    String detailBlock;
    String repositoryBlock;

    public void execute(Map<String, Object> root) {
        // 請勿砍掉 ↓↓↓↓↓↓↓
        // Map<String, Object> root = new HashMap<String,Object>();
        // root.put("ref_db_column", "material_actual_id");
        // root.put("java_lst_name", "materialActualProperties");
        // root.put("ref_java_type", "MaterialActualProperty"); // target entity
        // root.put("ref_java_name", "materialActualIdDynamic"); //from entity pk (for dynamic)
        // root.put("setter", "setMaterialActualProperties");
        // root.put("repository", "MaterialActualPropertyRepository");
        // root.put("method", "find4Relation");
        // 請勿砍掉 ↑↑↑↑↑↑↑
        try {
            if (root.containsKey("ref_db_column") && //
                    root.containsKey("java_lst_name") && //
                    root.containsKey("ref_java_type") && //
                    root.containsKey("ref_java_name") && //
                    root.containsKey("setter") && //
                    root.containsKey("repository") && //
                    root.containsKey("method") && //
                    true) {
                System.out.println("參數到齊!!");
            } else {
                throw new Exception("參數未到齊!!");
            }

            masterBlock = FreeMarkerSimpleUtil.replace(MASTER_BLOCK, root);
            detailBlock = FreeMarkerSimpleUtil.replace(DETAIL_BLOCK, root);
            repositoryBlock = FreeMarkerSimpleUtil.replace(REPOSITORY_BLOCK, root);

            System.out.println(masterBlock);
            System.out.println(detailBlock);
            System.out.println(repositoryBlock);
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

    public String getRepositoryBlock() {
        return repositoryBlock;
    }

    public static void main(String[] args) throws IOException, TemplateException {
        OneToManyCreaterTest t = new OneToManyCreaterTest();

        System.out.println("done...");
    }
}
