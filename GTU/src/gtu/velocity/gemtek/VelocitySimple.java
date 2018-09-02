package gtu.velocity.gemtek;

import gtu.file.FileUtil;
import gtu.string.StringUtilForDb;

import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

/**
 * @author Troy 2009/06/15
 * 
 */
public class VelocitySimple {

    Map<String, String> dbParam = new HashMap<String, String>();
    List<String> dbList = new ArrayList<String>();
    List<String> paramList = new ArrayList<String>();

    String originalStr = new String();
    String originalPk = new String();

    String[] fileList = null;

    private static StringBuffer sb = new StringBuffer().append("");

    public static void main(String[] args) {
        StringUtilForDb xx = new StringUtilForDb();
        // String driver = "oracle.jdbc.driver.OracleDriver";
        // String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        // String username = "EHS_V2";
        // String password = "EHS_V2";

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/ecweb";
        String username = "root";
        String password = "";

        Connection conn = null;
        VelocitySimple vs = new VelocitySimple();
        try {
            conn = xx.getConnection(driver, url, username, password);
            // List yy1 = xx.queryGetColumnName(sb.toString(), conn); //改這裡
            List yy1 = xx.queryColumnName("SELECT * FROM keyin_b2b", conn); // 改這裡
            vs.originalStr = "KeyinB2b"; // 改這裡
            vs.originalPk = "id"; // 改這裡
            vs.dbParam = xx.columnNameToVariableMap(yy1);
            vs.dbList = yy1;
            vs.paramList = xx.columnNameToVariable(yy1);
            vs.fileList = new String[] { "Role.xml", "RoleDao.java", "RoleDaoImpl.java", "RoleService.java",
                    "RoleServiceImpl.java", "RolePo.java", "RoleModel.xml" };
            vs.replaceSimple2();
            System.out.println("done...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Map putList(Map map, List list1, List list2) {
        for (int ii = 0; ii < list1.size(); ii++) {
            map.put(list1.get(ii), list2.get(ii));
        }
        return map;
    }

    private String originalString(String original) {
        original = original.toLowerCase();
        String[] split = original.split("_");
        original = "";
        for (int ii = 0; ii < split.length; ii++) {
            original += split[ii].substring(0, 1).toUpperCase() + split[ii].substring(1);
        }
        return original;
    }

    public void replaceSimple2() {
        try {
            // Velocity.addProperty("output.encoding","Big5");
            Velocity.init();
            VelocityContext context = new VelocityContext();
            StringWriter sw = new StringWriter();

            // Velocity.evaluate(context, sw, "com.compeq", new
            // String(sb.toString().getBytes(),"Big5"));
            String value = originalString(this.originalStr);
            String value2 = value.substring(0, 1).toLowerCase() + value.substring(1);
            String pk = originalString(this.originalPk);
            pk = pk.substring(0, 1).toLowerCase() + pk.substring(1);
            context.put("dbName", value2);
            context.put("poName", value2 + "Po");
            context.put("poNameBig", value + "Po");
            context.put("po", value);
            context.put("db", this.dbParam);
            context.put("dbList", this.dbList);
            context.put("dbNameBig", this.originalStr);
            context.put("pkCon", this.originalPk + " = #" + pk + "#");
            context.put("pk", pk);
            context.put("paramList", this.paramList);

            Template template = null;
            for (String fileStr : fileList) {
                // String front =
                // this.getClass().getResource("").getPath().substring(1);
                // front = front.replaceAll("bin", "src");
                template = Velocity.getTemplate(fileStr, "UTF8");
                sw = new StringWriter();
                template.merge(context, sw);
                String body = sw.getBuffer().toString();
                String fileName = fileStr.replaceAll("Role", context.get("po").toString());
                FileUtil.saveToFile("c:/" + fileName, body.getBytes());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void replaceSimple() {
        try {
            // initial Velocity Properties
            Properties props = new Properties();
            props.setProperty("resource.loader", "string");
            props.setProperty("string.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            props.setProperty("string.resource.loader.repository.class",
                    "org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl");

            // initial Velocity
            Velocity.init(props);

            // registry template name & body
            StringResourceRepository vsRepository = StringResourceLoader.getRepository();

            String templateName = "checkQspcMailVo.body";
            String templateBody = "$userName is ok!!";
            vsRepository.putStringResource(templateName, templateBody);

            // new velocityContext & put properties
            VelocityContext velocityContext = new VelocityContext();

            velocityContext.put("userName", "bbbb");

            // get velocity template
            Template template = Velocity.getTemplate(templateName, "UTF-8");

            // merge velocity Context & template
            StringWriter sw = new StringWriter();
            template.merge(velocityContext, sw);

            // set the merged body
            System.out.println(sw.getBuffer().toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
