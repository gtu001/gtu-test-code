package gtu.apache;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.Validate;

public class CommonLang {

    public static void main(String[] args) {
        CommonLang test = new CommonLang();

        //		test.testArrayUtils();
        test.testSystemUtils();

        //		org.apache.commons.lang.NumberUtils.
    }

    private void testStringEscapeUtils() {
        //		StringEscapeUtils.escapeHtml(str)
    }

    /**
     * Validate 驗證物件為空 拋錯
     */
    private void testValidate() {
        //1-若字串為空則拋錯  2-拋錯內容
        //1-可為 str map collection obj[]...
        String str = null;
        Validate.notEmpty(str, "str is null");
    }

    /**
     * RandomStringUtils 亂數產生字串
     */
    private void testRandomStringUtils() {
        //1=字元數   2=英文   3=數值
        System.out.println(RandomStringUtils.random(20, true, true));
        System.out.println(RandomStringUtils.random(20, false, true));
        System.out.println(RandomStringUtils.random(20, true, false));
        System.out.println(RandomStringUtils.random(20, "!@#$%^"));
    }

    /**
     * SystemUtils
     */
    private void testSystemUtils() {
        float requiredVersion = 1.6f;
        System.out.println(getLineNumber() + "=" + SystemUtils.AWT_TOOLKIT);
        System.out.println(getLineNumber() + "=" + SystemUtils.FILE_ENCODING);
        System.out.println(getLineNumber() + "=" + SystemUtils.FILE_SEPARATOR);
        System.out.println(getLineNumber() + "=" + SystemUtils.LINE_SEPARATOR);
        System.out.println(getLineNumber() + "=" + SystemUtils.IS_JAVA_1_5);
        System.out.println(getLineNumber() + "=" + SystemUtils.IS_JAVA_1_6);
        System.out.println(getLineNumber() + "=" + SystemUtils.IS_OS_WINDOWS_XP);
        System.out.println(getLineNumber() + "=" + SystemUtils.IS_OS_LINUX);
        System.out.println(getLineNumber() + "=" + SystemUtils.isJavaAwtHeadless());
        System.out.println(getLineNumber() + "=" + SystemUtils.isJavaVersionAtLeast(requiredVersion));
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_CLASS_PATH);
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_CLASS_VERSION);
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_COMPILER);
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_EXT_DIRS);
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_ENDORSED_DIRS);
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_IO_TMPDIR);
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_VM_NAME);
        System.out.println(getLineNumber() + "=" + SystemUtils.JAVA_VM_INFO);
        System.out.println(getLineNumber() + "=" + SystemUtils.OS_NAME);
        System.out.println(getLineNumber() + "=" + SystemUtils.USER_DIR);
        System.out.println(getLineNumber() + "=" + SystemUtils.USER_HOME);
        System.out.println(getLineNumber() + "=" + SystemUtils.USER_NAME);
        System.out.println(getLineNumber() + "=" + SystemUtils.USER_COUNTRY);
        System.out.println(getLineNumber() + "=" + SystemUtils.USER_LANGUAGE);
        System.out.println(getLineNumber() + "=" + SystemUtils.getJavaVersion());
        System.out.println(getLineNumber() + "=" + SystemUtils.getJavaHome());
        System.out.println(getLineNumber() + "=" + SystemUtils.getJavaIoTmpDir());
        System.out.println(getLineNumber() + "=" + SystemUtils.getUserDir());
        System.out.println(getLineNumber() + "=" + SystemUtils.getUserHome());
    }

    private static int getLineNumber() {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
        return stack.getLineNumber();
    }

    /**
     * 取得(包含)start到end的亂數
     * 
     * @param start
     * @param end
     * @return
     */
    public int getRandomInt(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }
}
