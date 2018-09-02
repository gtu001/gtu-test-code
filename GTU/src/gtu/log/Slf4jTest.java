package gtu.log;

import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.PropertyDefinerBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.StatusPrinter;

public class Slf4jTest {

    private Logger log1 = LoggerFactory.getLogger("aaa");
    private Logger log2 = LoggerFactory.getLogger("aaa.bbb");
    private Logger log3 = LoggerFactory.getLogger(Slf4jTest.class);
    private Logger log4 = LoggerFactory.getLogger("gtu.log");
    private Logger log5 = LoggerFactory.getLogger("gtu.log.test");

    public static void main(String[] args) throws Exception {
        Slf4jTest t = new Slf4jTest();
        t.test();
    }

    /**
     * 重設configuration 並且賦予一個設定檔案
     */
    public void resetConfiguration() {
        // assume SLF4J is bound to logback in the current environment
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            // Call context.reset() to clear any previous configuration, e.g. default 
            // configuration. For multi-step configuration, omit calling context.reset().
            context.reset();//清除所有設定值
            configurator.doConfigure(this.getClass().getResource("logback-test.xml"));//加入設定
        } catch (Exception je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }

    /**
     * 印出configuration 有沒有讀到的資訊
     */
    public void showConfigurationLoadingStatus() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    public void addListener() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusManager statusManager = lc.getStatusManager();
        OnConsoleStatusListener onConsoleListener = new OnConsoleStatusListener();
        statusManager.add(onConsoleListener);

        //或是設系統參數 不知有沒有效
        //        System.setProperty("logback.statusListenerClass", "ch.qos.logback.core.status.OnConsoleStatusListener");

        //        equal to...
        //        <configuration>
        //        <statusListener class="ch.qos.logback.core.status.OnConsoleStatusListener" />  
        //      </configuration>
    }

    private void test() throws ClassNotFoundException {
        //照道理會去抓這個設定值，但是無效
        //        System.setProperty("logback.configurationFile", this.getClass().getResource("logback-test.xml").getPath());

        resetConfiguration();

        //        showConfigurationLoadingStatus();
        System.out.println("######################");
        log1.debug("log1 array = {} {} {}", new String[] { "1", "2", "3" });
        System.out.println("######################");
        log2.debug("log2 array = {} {} {}", new String[] { "1", "2", "3" });
        System.out.println("######################");
        log3.debug("log3 array = {} {} {}", new String[] { "1", "2", "3" });
        System.out.println("######################");
        log4.debug("log4 array = {} {} {}", new String[] { "1", "2", "3" });
        System.out.println("######################");
        log5.debug("log5 array = {} {} {}", new String[] { "1", "2", "3" });
        System.out.println("######################");

        System.out.println("done...");
    }

    //    增加一個可以看Log status的servlet,按官方講法會產生html畫面於 http://host/yourWebapp/lbClassicStatus
    //    <servlet>
    //    <servlet-name>ViewStatusMessages</servlet-name>
    //    <servlet-class>ch.qos.logback.classic.ViewStatusMessagesServlet</servlet-class>
    //  </servlet>
    //  <servlet-mapping>
    //    <servlet-name>ViewStatusMessages</servlet-name>
    //    <url-pattern>/lbClassicStatus</url-pattern>
    //  </servlet-mapping>
    public static class TestConsoleAppender extends ConsoleAppender {
        public void start() {
            super.start();
            System.out.println("# start ...");
        }

        public void setTarget(String string) {
            super.setTarget(string);
            System.out.println("# setTarget ...");
            System.out.println("string = " + string);
        }

        public String getTarget() {
            String string = super.getTarget();
            System.out.println("# getTarget ...");
            return string;
        }

        public void stop() {
            super.stop();
            System.out.println("# stop ...");
        }

        public OutputStream getOutputStream() {
            OutputStream outputStream = super.getOutputStream();
            System.out.println("# getOutputStream ...");
            return outputStream;
        }

        public void setOutputStream(OutputStream outputStream) {
            super.setOutputStream(outputStream);
            System.out.println("# setOutputStream ...");
            System.out.println("outputStream = " + outputStream);
        }

        public void setLayout(Layout layout) {
            super.setLayout(layout);
            System.out.println("# setLayout ...");
            System.out.println("layout = " + layout);
        }

        public Encoder getEncoder() {
            Encoder encoder = super.getEncoder();
            System.out.println("# getEncoder ...");
            return encoder;
        }

        public void setEncoder(Encoder encoder) {
            super.setEncoder(encoder);
            System.out.println("# setEncoder ...");
            System.out.println("encoder = " + encoder);
        }

        public String toString() {
            String string = super.toString();
            System.out.println("# toString ...");
            return string;
        }

        public String getName() {
            String string = super.getName();
            System.out.println("# getName ...");
            return string;
        }

        public void setName(String string) {
            super.setName(string);
            System.out.println("# setName ...");
            System.out.println("string = " + string);
        }

        public boolean isStarted() {
            boolean bool = super.isStarted();
            System.out.println("# isStarted ...");
            return bool;
        }

        public void doAppend(Object object) {
            super.doAppend(object);
            System.out.println("# doAppend ...");
            System.out.println("object = " + object);
        }

        public void addFilter(Filter filter) {
            super.addFilter(filter);
            System.out.println("# addFilter ...");
            System.out.println("filter = " + filter);
        }

        public void clearAllFilters() {
            super.clearAllFilters();
            System.out.println("# clearAllFilters ...");
        }

        public List getCopyOfAttachedFiltersList() {
            List list = super.getCopyOfAttachedFiltersList();
            System.out.println("# getCopyOfAttachedFiltersList ...");
            return list;
        }

        public FilterReply getFilterChainDecision(Object object) {
            FilterReply filterReply = super.getFilterChainDecision(object);
            System.out.println("# getFilterChainDecision ...");
            System.out.println("object = " + object);
            return filterReply;
        }

        public Context getContext() {
            Context context = super.getContext();
            System.out.println("# getContext ...");
            return context;
        }

        public void addStatus(Status status) {
            super.addStatus(status);
            System.out.println("# addStatus ...");
            System.out.println("status = " + status);
        }

        public void addWarn(String string, Throwable throwable) {
            super.addWarn(string, throwable);
            System.out.println("# addWarn ...");
            System.out.println("string = " + string);
            System.out.println("throwable = " + throwable);
        }

        public void addWarn(String string) {
            super.addWarn(string);
            System.out.println("# addWarn ...");
            System.out.println("string = " + string);
        }

        public void setContext(Context context) {
            super.setContext(context);
            System.out.println("# setContext ...");
            System.out.println("context = " + context);
        }

        public void addError(String string) {
            super.addError(string);
            System.out.println("# addError ...");
            System.out.println("string = " + string);
        }

        public void addError(String string, Throwable throwable) {
            super.addError(string, throwable);
            System.out.println("# addError ...");
            System.out.println("string = " + string);
            System.out.println("throwable = " + throwable);
        }

        public void addInfo(String string, Throwable throwable) {
            super.addInfo(string, throwable);
            System.out.println("# addInfo ...");
            System.out.println("string = " + string);
            System.out.println("throwable = " + throwable);
        }

        public void addInfo(String string) {
            super.addInfo(string);
            System.out.println("# addInfo ...");
            System.out.println("string = " + string);
        }

        public StatusManager getStatusManager() {
            StatusManager statusManager = super.getStatusManager();
            System.out.println("# getStatusManager ...");
            return statusManager;
        }

        public boolean equals(Object object) {
            boolean bool = super.equals(object);
            System.out.println("# equals ...");
            System.out.println("object = " + object);
            return bool;
        }

        public int hashCode() {
            int int_ = super.hashCode();
            System.out.println("# hashCode ...");
            return int_;
        }
    }

    public static class TestPropertyDefiner extends PropertyDefinerBase {
        public Context getContext() {
            Context context = super.getContext();
            System.out.println("# getContext ...");
            return context;
        }

        public void setContext(Context context) {
            super.setContext(context);
            System.out.println("# setContext ...");
            System.out.println("context = " + context);
        }

        public void addStatus(Status status) {
            super.addStatus(status);
            System.out.println("# addStatus ...");
            System.out.println("status = " + status);
        }

        public void addInfo(String string) {
            super.addInfo(string);
            System.out.println("# addInfo ...");
            System.out.println("string = " + string);
        }

        public void addInfo(String string, Throwable throwable) {
            super.addInfo(string, throwable);
            System.out.println("# addInfo ...");
            System.out.println("string = " + string);
            System.out.println("throwable = " + throwable);
        }

        public void addWarn(String string) {
            super.addWarn(string);
            System.out.println("# addWarn ...");
            System.out.println("string = " + string);
        }

        public void addWarn(String string, Throwable throwable) {
            super.addWarn(string, throwable);
            System.out.println("# addWarn ...");
            System.out.println("string = " + string);
            System.out.println("throwable = " + throwable);
        }

        public void addError(String string) {
            super.addError(string);
            System.out.println("# addError ...");
            System.out.println("string = " + string);
        }

        public void addError(String string, Throwable throwable) {
            super.addError(string, throwable);
            System.out.println("# addError ...");
            System.out.println("string = " + string);
            System.out.println("throwable = " + throwable);
        }

        public StatusManager getStatusManager() {
            StatusManager statusManager = super.getStatusManager();
            System.out.println("# getStatusManager ...");
            return statusManager;
        }

        public boolean equals(Object object) {
            boolean bool = super.equals(object);
            System.out.println("# equals ...");
            System.out.println("object = " + object);
            return bool;
        }

        public String toString() {
            String string = super.toString();
            System.out.println("# toString ...");
            return string;
        }

        public int hashCode() {
            int int_ = super.hashCode();
            System.out.println("# hashCode ...");
            return int_;
        }

        public String getPropertyValue() {
            String string = "#############";
            System.out.println("# getPropertyValue ...");
            return string;
        }
    }
}
