package gtu.ireport.ex2;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class IReportCompileTest001 {

    public static void main(String[] args) throws JRException {
        String fromJrxml = "D:\\workstuff\\gtu-test-code\\GTU\\src\\gtu\\ireport\\ex2\\BG_B1Z540.jrxml";
        String destJasper = "D:\\workstuff\\gtu-test-code\\GTU\\src\\gtu\\ireport\\ex2\\BG_B1Z540.jasper";
        JasperCompileManager.compileReportToFile(fromJrxml, destJasper);
        System.out.println("done...");
    }

}
