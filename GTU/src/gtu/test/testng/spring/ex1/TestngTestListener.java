package gtu.test.testng.spring.ex1;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

/**
 * The parallel level is test instead of suite, class, method
 */
public class TestngTestListener implements ISuiteListener, ITestListener, IReporter {

    private static final Logger logger = LoggerFactory.getLogger(TestngTestListener.class);

    private static Long buildId;// lazy initialized for it's a scheduled bean to
                                // run checkAbnormalTestCase
    private static final ThreadLocal<Long> testId = new ThreadLocal<Long>();

    private static final ThreadLocal<Integer> userIndex = new ThreadLocal<Integer>();
    private static final Set<Integer> freePool = new HashSet<Integer>();
    private static final Set<Integer> workerPool = new HashSet<Integer>();

    public TestngTestListener() {
        try {
            Class<?> clazz = Class.forName("com.jprofiler.api.agent.Controller");
            Method startCpu = clazz.getMethod("startCPURecording", boolean.class);
            startCpu.invoke(null, true);
            Method startMonitor = clazz.getMethod("startMonitorRecording");
            startMonitor.invoke(null);
            File f = new File("profile.jps");
            Method exitMethod = clazz.getMethod("saveSnapshotOnExit", File.class);
            exitMethod.invoke(null, f);
        } catch (Exception e) {
            logger.info("{}", e.getMessage());
        }
    }

    public static Integer getUserIndex() {
        return userIndex.get();
    }

    @Override
    public void onFinish(ITestContext itestcontext) {
        // afterTest();
    }

    @Override
    public void onStart(ITestContext itestcontext) {
        // beforeTest();
    }

    private void afterTest() {
        synchronized (TestngTestListener.class) {
            Integer i = userIndex.get();
            if (i == null) {// it seems skipped
                return;
            }
            workerPool.remove(i);
            freePool.add(i);
            userIndex.set(null);
        }
    }

    private void beforeTest() {
        synchronized (TestngTestListener.class) {
            int i;
            if (freePool.isEmpty()) {
                i = workerPool.size() + 1;// starts from 1
            } else {
                i = freePool.iterator().next();
                freePool.remove(i);
            }
            workerPool.add(i);
            userIndex.set(i);
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult itestresult) {
        afterTest();
    }

    @Override
    public void onTestSkipped(ITestResult itestresult) {
        afterTest();
    }

    @Override
    public void onTestFailure(ITestResult itestresult) {
        afterTest();
    }

    @Override
    public void onTestSuccess(ITestResult itestresult) {
        afterTest();
    }

    @Override
    public void onTestStart(ITestResult itestresult) {
        beforeTest();
    }

    @Override
    public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1, String directory) {
    }

    @Override
    public void onStart(ISuite isuite) {
    }

    @Override
    public void onFinish(ISuite isuite) {
    }
    
    public void checkAbnormalTestCase(){
    }
}
