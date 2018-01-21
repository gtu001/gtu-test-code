package gtu.velocity.builder;

import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import gtu.javafx.traynotification.NotificationType;

public class BuilderCreater {

    public static void main(String[] args) {
        Set<Pair<String, String>> paramLst = new LinkedHashSet<Pair<String, String>>();
        paramLst.add(ImmutablePair.of("String", "title"));
        paramLst.add(ImmutablePair.of("String", "message"));
        paramLst.add(ImmutablePair.of("NotificationType", "notification"));
        paramLst.add(ImmutablePair.of("AnimationType", "animationType"));
        paramLst.add(ImmutablePair.of("EventHandler", "onPanelClickCallback"));
        paramLst.add(ImmutablePair.of("Image", "image"));
        paramLst.add(ImmutablePair.of("String", "rectangleFill"));
        
        String className = "TrayNotificationHelper";
        String result = BuilderCreater.newInstance().className(className).paramLst(paramLst).build();
        System.out.println(result);
        System.out.println("done...");
    }

    private BuilderCreater() {
    }

    private Set<Pair<String, String>> paramLst;
    private String className;

    public BuilderCreater paramLst(Set<Pair<String, String>> paramLst) {
        this.paramLst = paramLst;
        return this;
    }

    public BuilderCreater className(String className) {
        this.className = className;
        return this;
    }

    public static BuilderCreater newInstance() {
        return new BuilderCreater();
    }

    public String build() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("public class {0} '{'\n");
            sb.append("private {0}() '{'}\n");
            sb.append(" public static {0} newInstance() '{'   \n");
            sb.append("         return new {0}();           \n");
            sb.append(" }                                              \n");
            sb.append("{1}\n");
            sb.append("{2}\n");
            sb.append("  public void build() '{'                      \n");
            sb.append("      try '{'                                  \n");
            sb.append("      } catch (Exception ex) '{'               \n");
            sb.append("          throw new RuntimeException(ex);    \n");
            sb.append("      }                                      \n");
            sb.append("  }                                          \n");
            sb.append("}\n");
            String classFormat = sb.toString();

            sb = new StringBuilder();
            sb.append("    public {2} {0}({1} {0}) '{'    \n");
            sb.append("        this.{0} = {0};           \n");
            sb.append("        return this;              \n");
            sb.append("    }                             \n");
            String methodFormat = sb.toString();
            String paramFormat = "private {1} {0};\n";

            StringBuilder methodSb = new StringBuilder();
            StringBuilder paramSb = new StringBuilder();
            for (Pair<String, String> pair : paramLst) {
                String paramName = pair.getValue();
                String paramType = pair.getKey();
                methodSb.append(MessageFormat.format(methodFormat, new Object[] { paramName, paramType, className }));
                paramSb.append(MessageFormat.format(paramFormat, new Object[] { paramName, paramType }));
            }
            return MessageFormat.format(classFormat, new Object[] { className, paramSb, methodSb });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
