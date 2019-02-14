package gtu.enum_;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rl08220ReportConstant {

    public static void main(String[] args) {
        System.out.println(getReportNameList("z", ReportClass.Born, TaskClass.RC));
    }

    private static Logger log = LoggerFactory.getLogger(Rl08220ReportConstant.class);

    public enum TaskClass {
        RC, RR, RL;
    }

    public enum ReportClass {
        Born(BornReportNum.values()), //
        Dead(DeadReportNum.values()), //
        Marriage(MarriageReportNum.values()), //
        Divorce(DivorceReportNum.values()), //
        ;//
        final Enum<?>[] enus;

        ReportClass(Enum<?>[] enus) {
            this.enus = enus;
        }
    }

    enum BornReportNum {
        R1("出生動態統計表（1）", "RLRP08221", "RRRP03211", "RCRP0C511"), //
        R2("出生動態統計表（2）", "RLRP08222", "RRRP03212", "RCRP0C512"), //
        R3("出生動態統計表（3）", "RLRP08223", "RRRP03213", "RCRP0C513"), //
        R4("出生動態統計表（4）", "RLRP08224", "RRRP03214", "RCRP0C514"), //
        R5("出生動態統計表（5）", "RLRP08225", "RRRP03215", "RCRP0C515"), //
        R6("出生動態統計表（6）", "RLRP08226", "RRRP03216", "RCRP0C516"), //
        R7("出生動態統計表（7）", "RLRP08227", "RRRP03217", "RCRP0C517"), //
        R8("出生動態統計表（8）", "RLRP08228", "RRRP03218", "RCRP0C518"), //
        R9("出生動態統計表（9）", "RLRP08229", "RRRP03219", "RCRP0C519"), //
        R10("出生動態統計表（10）", "RLRP0822A", "RRRP0321A", "RCRP0C51A"), //
        R11("出生動態統計表（11）", "RLRP0822B", "RRRP0321B", "RCRP0C51B"), //
        R12("出生動態統計表（12）", "RLRP0822C", "RRRP0321C", "RCRP0C51C"), //
        R13("出生動態統計表（13）", "RLRP0822D", "RRRP0321D", "RCRP0C51D"), //
        R14("出生動態統計表（14）", "RLRP0822E", "RRRP0321E", "RCRP0C51E"), //
        R15("出生動態統計表（15）", "RLRP0822F", "RRRP0321F", "RCRP0C51F"), //
        R16("出生動態統計表（16）", "RLRP0822G", "RRRP0321G", "RCRP0C51G"), //
        R17("出生動態統計表（17）", "RLRP0822H", "RRRP0321H", "RCRP0C51H"), //
        R18("出生動態統計表（18）", "RLRP0822I", "RRRP0321I", "RCRP0C51I"), //
        R19("出生動態統計表（19）", "RLRP0822J", "RRRP0321J", "RCRP0C51J"), //
        R20("出生動態統計表（20）", "RLRP0822K", "RRRP0321K", "RCRP0C51K"), //
        R21("出生動態統計表（21）", "RLRP0822L", "RRRP0321L", "RCRP0C51L"), //
        R22("出生動態統計表（22）", "RLRP0822M", "RRRP0321M", "RCRP0C51M"), //
        R23("出生動態統計表（23）", "RLRP0822N", "RRRP0321N", "RCRP0C51N"), //
        R24("出生動態統計表（24）", "RLRP0822O", "RRRP0321O", "RCRP0C51O"), //
        R25("出生動態統計表（25）", "RLRP0822P", "RRRP0321P", "RCRP0C51P"), //
        R26("出生動態統計表（26）", "RLRP0822Q", "RRRP0321Q", "RCRP0C51Q"), //
        R27("出生動態統計表（27）", "RLRP0822R", "RRRP0321R", "RCRP0C51R"), //
        ;
        final String desc;
        final String rl;
        final String rr;
        final String rc;

        BornReportNum(String desc, String rl, String rr, String rc) {
            this.desc = desc;
            this.rl = rl;
            this.rr = rr;
            this.rc = rc;
        }
    }

    enum DeadReportNum {
        R1("死亡動態統計表（1）", "RLRP08231", "RRRP03221", "RCRP0C521"), //
        R2("死亡動態統計表（2）", "RLRP08232", "RRRP03222", "RCRP0C522"), //
        R3("死亡動態統計表（3）", "RLRP08233", "RRRP03223", "RCRP0C523"), //
        R4("死亡動態統計表（4）", "RLRP08234", "RRRP03224", "RCRP0C524"), //
        R5("死亡動態統計表（5）", "RLRP08235", "RRRP03225", "RCRP0C525"), //
        R6("死亡動態統計表（6）", "RLRP08236", "RRRP03226", "RCRP0C526"), //
        R7("死亡動態統計表（7）", "RLRP08237", "RRRP03227", "RCRP0C527"), //
        ;
        final String desc;
        final String rl;
        final String rr;
        final String rc;

        DeadReportNum(String desc, String rl, String rr, String rc) {
            this.desc = desc;
            this.rl = rl;
            this.rr = rr;
            this.rc = rc;
        }
    }

    enum MarriageReportNum {
        R1("結婚動態統計表（1）", "RLRP08241", "RRRP03231", "RCRP0C531"), //
        R2("結婚動態統計表（2）", "RLRP08242", "RRRP03232", "RCRP0C532"), //
        R3("結婚動態統計表（3）", "RLRP08243", "RRRP03233", "RCRP0C533"), //
        R4("結婚動態統計表（4）", "RLRP08244", "RRRP03234", "RCRP0C534"), //
        R5("結婚動態統計表（5）", "RLRP08245", "RRRP03235", "RCRP0C535"), //
        R6("結婚動態統計表（6）", "RLRP08246", "RRRP03236", "RCRP0C536"), //
        R7("結婚動態統計表（7）", "RLRP08247", "RRRP03237", "RCRP0C537"), //
        R8("結婚動態統計表（8）", "RLRP08248", "RRRP03238", "RCRP0C538"), //
        R9("結婚動態統計表（9）", "RLRP08249", "RRRP03239", "RCRP0C539"), //
        R10("結婚動態統計表（10）", "RLRP0824A", "RRRP0323A", "RCRP0C53A"), //
        R11("結婚動態統計表（11）", "RLRP0824B", "RRRP0323B", "RCRP0C53B"), //
        R12("結婚動態統計表（12）", "RLRP0824C", "RRRP0323C", "RCRP0C53C"), //
        R13("結婚動態統計表（13）", "RLRP0824D", "RRRP0323D", "RCRP0C53D"), //
        R14("結婚動態統計表（14）", "RLRP0824E", "RRRP0323E", "RCRP0C53E"), //
        R15("結婚動態統計表（15）", "RLRP0824F", "RRRP0323F", "RCRP0C53F"), //
        R16("結婚動態統計表（16）", "RLRP0824G", "RRRP0323G", "RCRP0C53G"), //
        R17("結婚動態統計表（17）", "RLRP0824H", "RRRP0323H", "RCRP0C53H"), //
        ;
        final String desc;
        final String rl;
        final String rr;
        final String rc;

        MarriageReportNum(String desc, String rl, String rr, String rc) {
            this.desc = desc;
            this.rl = rl;
            this.rr = rr;
            this.rc = rc;
        }
    }

    enum DivorceReportNum {
        R1("離婚動態統計表（1）", "RLRP08251", "RRRP03281", "RCRP0C581"), //
        R2("離婚動態統計表（2）", "RLRP08252", "RRRP03242", "RCRP0C542"), //
        R3("離婚動態統計表（3）", "RLRP08253", "RRRP03243", "RCRP0C543"), //
        R4("離婚動態統計表（4）", "RLRP08254", "RRRP03244", "RCRP0C546"), //
        R5("離婚動態統計表（5）", "RLRP08255", "RRRP03245", "RCRP0C545"), //
        R6("離婚動態統計表（6）", "RLRP08256", "RRRP03246", "RCRP0C546"), //
        R7("離婚動態統計表（7）", "RLRP08257", "RRRP03247", "RCRP0C547"), //
        R8("離婚動態統計表（8）", "RLRP08258", "RRRP03248", "RCRP0C548"), //
        R9("離婚動態統計表（9）", "RLRP08259", "RRRP03249", "RCRP0C549"), //
        R12("離婚動態統計表（12）", "RLRP0825C", "RRRP0324C", "RCRP0C54C"), //
        R13("離婚動態統計表（13）", "RLRP0825D", "RRRP0324D", "RCRP0C54D"), //
        R14("離婚動態統計表（14）", "RLRP0825E", "RRRP0324E", "RCRP0C54E"), //
        R15("離婚動態統計表（15）", "RLRP0825F", "RRRP0324F", "RCRP0C54F"), //
        ;
        final String desc;
        final String rl;
        final String rr;
        final String rc;

        DivorceReportNum(String desc, String rl, String rr, String rc) {
            this.desc = desc;
            this.rl = rl;
            this.rr = rr;
            this.rc = rc;
        }
    }

    public static List<?> getDropdownList(ReportClass reportClass, TaskClass taskClass) {
        log.debug("#. getDropdownList .s");

        log.debug("reportClass = {}", reportClass);
        log.debug("taskClass = {}", taskClass);

        if (reportClass == null) {
            throw new RuntimeException("reportClass不可為空!");
        }
        if (taskClass == null) {
            throw new RuntimeException("taskClass不可為空!");
        }

        try {
            List<Object> list = new ArrayList<Object>();
            Class<?> clz = Class.forName("javax.faces.model.SelectItem");
            Constructor<?> constructor = clz.getDeclaredConstructor(Object.class, String.class);
            for (Enum<?> e : reportClass.enus) {
                String reportName = getReportName(e, taskClass);
                String desc = getFieldName(e, "desc");
                list.add(constructor.newInstance(reportName, desc));
            }
            list.add(constructor.newInstance("z", "全部"));
            return list;
        } catch (Exception e) {
            log.error("error", e);
            throw new RuntimeException(e);
        } finally {
            log.debug("#. getDropdownList .e");
        }
    }

    /**
     * @param id
     *            對應報表的數字 Ex: id="5" => "離婚動態統計表（5）"
     * @param reportClass
     *            是取得(出生,死亡,結婚,離婚 其中一項)哪種報表
     * @param taskClass
     *            是取得(RC,RR,RL 其中一項)哪種報表
     * @return
     */
    public static List<String> getReportNameList(String id, ReportClass reportClass, TaskClass taskClass) {
        log.debug("#. getReportNameList .s");

        log.debug("id = {}", id);
        log.debug("reportClass = {}", reportClass);
        log.debug("taskClass = {}", taskClass);

        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("id不可為空!");
        }
        if (reportClass == null) {
            throw new RuntimeException("reportClass不可為空!");
        }
        if (taskClass == null) {
            throw new RuntimeException("taskClass不可為空!");
        }

        List<String> list = new ArrayList<String>();

        if ("z".equals(id)) {
            for (Enum<?> e : reportClass.enus) {
                String reportName = getReportName(e, taskClass);
                list.add(reportName);
            }
        } else {
            for (Enum<?> e : reportClass.enus) {
                String name = e.name().replaceFirst("R", "");
                if (name.equals(id)) {
                    String reportName = getReportName(e, taskClass);
                    list.add(reportName);
                    break;
                }
            }
        }

        log.debug("report = {}", list);

        log.debug("#. getReportNameList .e");
        return list;
    }

    private static String getReportName(Enum<?> e, TaskClass taskClass) {
        try {
            String taskStr = taskClass.name().toLowerCase();
            Field f = e.getClass().getDeclaredField(taskStr);
            String value = (String) f.get(e);
            return value;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new RuntimeException(ex);
        }
    }

    private static String getFieldName(Enum<?> e, String fieldName) {
        try {
            Field f = e.getClass().getDeclaredField(fieldName);
            String value = (String) f.get(e);
            return value;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new RuntimeException(ex);
        }
    }
}