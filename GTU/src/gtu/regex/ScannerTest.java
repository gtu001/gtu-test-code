package gtu.regex;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
public class ScannerTest {

    private Logger log = Logger.getLogger(getClass());
    private Scanner scan = null;

    public static void main(String[] args) throws Exception {
        ScannerTest test = new ScannerTest();
        test.writeClassInfo();
    }

    @Ignore
    public void writeClassInfo() throws Exception {
        Scanner scan = new Scanner("aaaaaandbbbbbbandvvvvvvvandsdfsdfasdfasfdandflsjfdwjeoriand");
        Pattern pattern = scan.delimiter();
        System.out.println("pattern = [" + pattern.pattern() + "]");

        scan.useDelimiter("and");

        while (scan.hasNext()) {
            String str = scan.next();
            System.out.println(str);
        }
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // log.debug("# setUpBeforeClass ...");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // log.debug("# tearDownAfterClass ...");
    }

    @Before
    public void setUp() throws Exception {
        // log.debug("# setUp ...");
        scan = new Scanner();
    }

    @After
    public void tearDown() throws Exception {
        // log.debug("# tearDown ...");
        scan = null;
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    // PUBLIC
    /**
     * 返回此扫描器的默认基数。
     */
    // @Ignore
    @org.junit.Test
    public void testRadix() throws Exception {
        log.debug("# testRadix ...");
        log.debug("\tradix = " + scan.radix());
    }

    /**
     * 将输入信息的下一个标记扫描为一个 short。
     */
    // @Ignore
    @org.junit.Test
    public void testNextShort() throws Exception {
        log.debug("# testNextShort ...");
        log.debug("\tnextShort = " + scan.nextShort(int));
    /**
    * 将输入信息的下一个标记扫描为一个 short。
    */
        log.debug("\tnextShort = " + scan.nextShort());
    }

    /**
     * 如果下一个标记与从指定字符串构造的模式匹配，则返回 true。
     */
    // @Ignore
    @org.junit.Test
    public void testHasNext() throws Exception {
        log.debug("# testHasNext ...");
        log.debug("\thasNext = " + scan.hasNext(java.lang.String));
        /**
         * 如果下一个标记与从指定字符串构造的模式匹配，则返回 true。
         */
        log.debug("\thasNext = " + scan.hasNext());
        /**
         * 如果下一个标记与从指定字符串构造的模式匹配，则返回 true。
         */
        log.debug("\thasNext = " + scan.hasNext(java.util.regex.Pattern));
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextByte() throws Exception {
        log.debug("# testHasNextByte ...");
        log.debug("\thasNextByte = " + scan.hasNextByte());
    /**
    * null
    */
        log.debug("\thasNextByte = " + scan.hasNextByte(int));
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextDouble() throws Exception {
        log.debug("# testHasNextDouble ...");
        log.debug("\thasNextDouble = " + scan.hasNextDouble());
    }

    /**
     * 返回此 Scanner 的底层 Readable 最后抛出的 IOException。
     */
    // @Ignore
    @org.junit.Test
    public void testIoException() throws Exception {
        log.debug("# testIoException ...");
        log.debug("\tioException = " + scan.ioException());
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextFloat() throws Exception {
        log.debug("# testHasNextFloat ...");
        log.debug("\thasNextFloat = " + scan.hasNextFloat());
    }

    /**
     * 返回此 Scanner 当前正在用于匹配分隔符的 Pattern。
     */
    // @Ignore
    @org.junit.Test
    public void testDelimiter() throws Exception {
        log.debug("# testDelimiter ...");
        log.debug("\tdelimiter = " + scan.delimiter());
    }

    /**
     * 将输入信息的下一个标记扫描为一个 int。
     */
    // @Ignore
    @org.junit.Test
    public void testNextInt() throws Exception {
        log.debug("# testNextInt ...");
        log.debug("\tnextInt = " + scan.nextInt(int));
    /**
    * 将输入信息的下一个标记扫描为一个 int。
    */
        log.debug("\tnextInt = " + scan.nextInt());
    }

    /**
     * 试图在忽略分隔符的情况下查找下一个从指定字符串构造的模式。
     */
    // @Ignore
    @org.junit.Test
    public void testFindWithinHorizon() throws Exception {
        log.debug("# testFindWithinHorizon ...");
        log.debug("\tfindWithinHorizon = " + scan.findWithinHorizon(java.lang.String, int));
    /**
    * 试图在忽略分隔符的情况下查找下一个从指定字符串构造的模式。
    */
        log.debug("\tfindWithinHorizon = " + scan.findWithinHorizon(java.util.regex.Pattern, int));
    }

    /**
     * 将输入信息的下一个标记扫描为一个 float。
     */
    // @Ignore
    @org.junit.Test
    public void testNextFloat() throws Exception {
        log.debug("# testNextFloat ...");
        log.debug("\tnextFloat = " + scan.nextFloat());
    }

    /**
     * 返回此扫描器所执行的最后扫描操作的匹配结果。
     */
    // @Ignore
    @org.junit.Test
    public void testMatch() throws Exception {
        log.debug("# testMatch ...");
        log.debug("\tmatch = " + scan.match());
    }

    /**
     * 返回此 Scanner 的字符串表示形式。
     */
    // @Ignore
    @org.junit.Test
    public void testToString() throws Exception {
        log.debug("# testToString ...");
        log.debug("\ttoString = " + scan.toString());
    }

    /**
     * 试图在忽略分隔符的情况下查找下一个从指定字符串构造的模式。
     */
    // @Ignore
    @org.junit.Test
    public void testFindInLine() throws Exception {
        log.debug("# testFindInLine ...");
        log.debug("\tfindInLine = " + scan.findInLine(java.util.regex.Pattern));
        /**
         * 试图在忽略分隔符的情况下查找下一个从指定字符串构造的模式。
         */
        log.debug("\tfindInLine = " + scan.findInLine(java.lang.String));
    }

    /**
     * 将输入信息的下一个标记扫描为一个 long。
     */
    // @Ignore
    @org.junit.Test
    public void testNextLong() throws Exception {
        log.debug("# testNextLong ...");
        log.debug("\tnextLong = " + scan.nextLong());
    /**
    * 将输入信息的下一个标记扫描为一个 long。
    */
        log.debug("\tnextLong = " + scan.nextLong(int));
    }

    /**
     * 此扫描器执行当前行，并返回跳过的输入信息。
     */
    // @Ignore
    @org.junit.Test
    public void testNextLine() throws Exception {
        log.debug("# testNextLine ...");
        log.debug("\tnextLine = " + scan.nextLine());
    }

    /**
     * 将此扫描器的分隔模式设置为从指定 String 构造的模式。
     */
    // @Ignore
    @org.junit.Test
    public void testUseDelimiter() throws Exception {
        log.debug("# testUseDelimiter ...");
        log.debug("\tuseDelimiter = " + scan.useDelimiter(java.util.regex.Pattern));
        /**
         * 将此扫描器的分隔模式设置为从指定 String 构造的模式。
         */
        log.debug("\tuseDelimiter = " + scan.useDelimiter(java.lang.String));
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextBigInteger() throws Exception {
        log.debug("# testHasNextBigInteger ...");
        log.debug("\thasNextBigInteger = " + scan.hasNextBigInteger(int));
    /**
    * null
    */
        log.debug("\thasNextBigInteger = " + scan.hasNextBigInteger());
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextBigDecimal() throws Exception {
        log.debug("# testHasNextBigDecimal ...");
        log.debug("\thasNextBigDecimal = " + scan.hasNextBigDecimal());
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextLong() throws Exception {
        log.debug("# testHasNextLong ...");
        log.debug("\thasNextLong = " + scan.hasNextLong(int));
    /**
    * null
    */
        log.debug("\thasNextLong = " + scan.hasNextLong());
    }

    /**
     * 将输入信息的下一个标记扫描为一个 BigDecimal。
     */
    // @Ignore
    @org.junit.Test
    public void testNextBigDecimal() throws Exception {
        log.debug("# testNextBigDecimal ...");
        log.debug("\tnextBigDecimal = " + scan.nextBigDecimal());
    }

    /**
     * 将输入信息的下一个标记扫描为一个 byte。
     */
    // @Ignore
    @org.junit.Test
    public void testNextByte() throws Exception {
        log.debug("# testNextByte ...");
        log.debug("\tnextByte = " + scan.nextByte());
    /**
    * 将输入信息的下一个标记扫描为一个 byte。
    */
        log.debug("\tnextByte = " + scan.nextByte(int));
    }

    /**
     * 将输入信息的下一个标记扫描为一个 double。
     */
    // @Ignore
    @org.junit.Test
    public void testNextDouble() throws Exception {
        log.debug("# testNextDouble ...");
        log.debug("\tnextDouble = " + scan.nextDouble());
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testUseRadix() throws Exception {
        log.debug("# testUseRadix ...");
        log.debug("\tuseRadix = " + scan.useRadix(int));
    }

    /**
     * 扫描解释为一个布尔值的输入标记并返回该值。
     */
    // @Ignore
    @org.junit.Test
    public void testNextBoolean() throws Exception {
        log.debug("# testNextBoolean ...");
        log.debug("\tnextBoolean = " + scan.nextBoolean());
    }

    /**
     * 重置此扫描器。
     */
    // @Ignore
    @org.junit.Test
    public void testReset() throws Exception {
        log.debug("# testReset ...");
        log.debug("\treset = " + scan.reset());
    }

    /**
     * 如果在此扫描器的输入中存在另一行，则返回 true。
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextLine() throws Exception {
        log.debug("# testHasNextLine ...");
        log.debug("\thasNextLine = " + scan.hasNextLine());
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextShort() throws Exception {
        log.debug("# testHasNextShort ...");
        log.debug("\thasNextShort = " + scan.hasNextShort());
    /**
    * null
    */
        log.debug("\thasNextShort = " + scan.hasNextShort(int));
    }

    /**
     * 将此扫描器的语言环境设置为指定的语言环境。
     */
    // @Ignore
    @org.junit.Test
    public void testUseLocale() throws Exception {
        log.debug("# testUseLocale ...");
        log.debug("\tuseLocale = " + scan.useLocale(java.util.Locale));
    }

    /**
     * 返回此扫描器的语言环境。
     */
    // @Ignore
    @org.junit.Test
    public void testLocale() throws Exception {
        log.debug("# testLocale ...");
        log.debug("\tlocale = " + scan.locale());
    }

    /**
     * 如果下一个标记与从指定字符串构造的模式匹配，则返回下一个标记。
     */
    // @Ignore
    @org.junit.Test
    public void testNext() throws Exception {
        log.debug("# testNext ...");
        log.debug("\tnext = " + scan.next());
        /**
         * 如果下一个标记与从指定字符串构造的模式匹配，则返回下一个标记。
         */
        log.debug("\tnext = " + scan.next());
        /**
         * 如果下一个标记与从指定字符串构造的模式匹配，则返回下一个标记。
         */
        log.debug("\tnext = " + scan.next(java.lang.String));
        /**
         * 如果下一个标记与从指定字符串构造的模式匹配，则返回下一个标记。
         */
        log.debug("\tnext = " + scan.next(java.util.regex.Pattern));
    }

    /**
     * Iterator 的这种实现不支持移除操作。
     */
    // @Ignore
    @org.junit.Test
    public void testRemove() throws Exception {
        log.debug("# testRemove ...");
        scan.remove();
    }

    /**
     * 关闭此扫描器。
     */
    // @Ignore
    @org.junit.Test
    public void testClose() throws Exception {
        log.debug("# testClose ...");
        scan.close();
    }

    /**
     * 如果通过使用一个从字符串 "true|false" 创建的大小写敏感的模式，此扫描器输入信息中的下一个标记可以解释为一个布尔值，则返回 true。
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextBoolean() throws Exception {
        log.debug("# testHasNextBoolean ...");
        log.debug("\thasNextBoolean = " + scan.hasNextBoolean());
    }

    /**
     * null
     */
    // @Ignore
    @org.junit.Test
    public void testHasNextInt() throws Exception {
        log.debug("# testHasNextInt ...");
        log.debug("\thasNextInt = " + scan.hasNextInt(int));
    /**
    * null
    */
        log.debug("\thasNextInt = " + scan.hasNextInt());
    }

    /**
     * 跳过与从指定字符串构造的模式匹配的输入信息。
     */
    // @Ignore
    @org.junit.Test
    public void testSkip() throws Exception {
        log.debug("# testSkip ...");
        log.debug("\tskip = " + scan.skip(java.util.regex.Pattern));
        /**
         * 跳过与从指定字符串构造的模式匹配的输入信息。
         */
        log.debug("\tskip = " + scan.skip(java.lang.String));
    }

    /**
     * 将输入信息的下一个标记扫描为一个 BigInteger。
     */
    // @Ignore
    @org.junit.Test
    public void testNextBigInteger() throws Exception {
        log.debug("# testNextBigInteger ...");
        log.debug("\tnextBigInteger = " + scan.nextBigInteger());
    /**
    * 将输入信息的下一个标记扫描为一个 BigInteger。
    */
        log.debug("\tnextBigInteger = " + scan.nextBigInteger(int));
    }
}
