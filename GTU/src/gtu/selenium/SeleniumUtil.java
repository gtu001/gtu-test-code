package gtu.selenium;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import gtu.file.OsInfoUtil;

public class SeleniumUtil {

    private static final SeleniumUtil _INST = new SeleniumUtil();

    private static final long WAIT_IN_SECONDS = 120L;

    public static SeleniumUtil getInstance() {
        return _INST;
    }

    public static <V> void main(String[] args) {
        SeleniumUtil util = SeleniumUtil.getInstance();
        WebDriver driver = util.getDriver();
        driver.get("https://developer.mozilla.org/en-US/docs/Web/HTML/Element/select");
        WebElement elem = WebElementControl.waitPageElementByXpath("//select[@id=pet-select]", driver);
        System.out.println(">>>>>>>" + elem);
        System.out.println("done...");
    }

    public WebDriver getDriver() {
        return getDriver("");
    }

    public WebDriver getDriver(String driverPath) {
        if (StringUtils.isBlank(driverPath)) {
            if (OsInfoUtil.isWindows()) {
                driverPath = "D:/apps/selenium/chromedriver.exe";
            } else {
                driverPath = "/media/gtu001/OLD_D/apps/webdriver/chromedriver";
            }
        }
        System.setProperty("webdriver.chrome.driver", driverPath);// chrome
        System.setProperty("webdriver.gecko.driver", driverPath);// firefox
        // WebDriver driver = new FirefoxDriver();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        // 設定長寬
        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().setSize(new Dimension(1024, 768));
        return driver;
    }

    public static class WebElementControl {

        public static void setValue(WebElement element, String value) {
            element.sendKeys(value);
        }

        public static String getValue(WebElement element) {
            return element.getAttribute("value");
        }

        public static void clearInput(WebElement element) {
            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(Keys.DELETE);
        }

        public static void enter(WebElement element) {
            element.sendKeys(Keys.ENTER);
        }

        public static String getHtml(WebElement element) {
            return element.getAttribute("innerHTML");
        }

        public static WebElement getParent(WebElement childElement, WebDriver driver) {
            JavascriptExecutor executor = (JavascriptExecutor)driver;
            WebElement parentElement = (WebElement)executor.executeScript("return arguments[0].parentNode;", childElement);
            return parentElement;
        }
        
        public static String getCurrentUrl(WebDriver driver) {
            return driver.getCurrentUrl();
        }

        public static void setSelect(WebElement element, String text, String value, Integer index) {
            if (StringUtils.isNotBlank(text)) {
                element.findElement(By.xpath(".//option[text()='" + text + "']")).click();
            } else if (StringUtils.isNotBlank(value)) {
                element.findElement(By.xpath(".//option[value()='" + text + "']")).click();
            } else {
                boolean findOk = false;
                List<WebElement> options = element.findElements(By.xpath(".//option"));
                for (int i = 0; i < options.size(); i++) {
                    WebElement v = options.get(i);
                    if (i == index) {
                        findOk = true;
                        System.out.println("setSelect(index) = " + i + ", value=" + v.getAttribute("value") + ", text=" + v.getText());
                        v.click();
                    }
                }
                if (!findOk) {
                    System.out.println("index 超出範圍 : " + index);
                }
            }
        }

        public static List<Pair<String, String>> getOptionsLst(WebElement element) {
            List<Pair<String, String>> lst = new ArrayList<Pair<String, String>>();
            List<WebElement> options = element.findElements(By.xpath(".//option"));
            for (int i = 0; i < options.size(); i++) {
                WebElement v = options.get(i);
                System.out.println(i + ", value=" + v.getAttribute("value") + ", text=" + v.getText());
                lst.add(Pair.of(v.getAttribute("value"), v.getText()));
            }
            return lst;
        }

        public static void until(WebDriver driver, int timeout, final java.util.function.Function<WebDriver, Boolean> func) {
            new WebDriverWait(driver, timeout).until(//
                    new java.util.function.Function<WebDriver, Boolean>() {
                        @Override
                        public Boolean apply(WebDriver t) {
                            return func.apply(t);
                        }
                    });
        }

        public static void clickUntil(WebDriver driver, String xpath, String css) {
            for (int t = 0; t < 20; t++) {
                try {
                    if (StringUtils.isNotBlank(xpath)) {
                        new WebDriverWait(driver, WAIT_IN_SECONDS).until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
                    } else if (StringUtils.isNotBlank(css)) {
                        new WebDriverWait(driver, WAIT_IN_SECONDS).until(ExpectedConditions.elementToBeClickable(By.cssSelector(css))).click();
                    }
                } catch (ElementClickInterceptedException ex) {
                    _TimeSleep_(1000);
                }
            }
        }

        private static void _TimeSleep_(long time) {
            try {
                Thread.currentThread().sleep(time);
            } catch (InterruptedException e) {
            }
        }

        public static WebElement waitPageElementByCss(final String css, final String text, final WebDriver driver) {
            final int retryTime = 10;
            final long retryWait = 500;
            return waitPageElementByCss(css, text, driver, retryTime, retryWait);
        }

        public static WebElement waitPageElementByCss(final String css, final String text, final WebDriver driver, final int retryTime, final long retryWait) {
            for (int t = 0; t < retryTime; t++) {
                try {
                    new WebDriverWait(driver, WAIT_IN_SECONDS).until(//
                            new java.util.function.Function<WebDriver, Boolean>() {
                                @Override
                                public Boolean apply(WebDriver t) {
                                    List<WebElement> elems = driver.findElements(By.cssSelector(css));
                                    System.out.println("check_css_exists = " + elems.size());
                                    if (elems.isEmpty()) {
                                        return false;
                                    }
                                    return true;
                                }
                            });
                    List<WebElement> elems = driver.findElements(By.cssSelector(css));
                    for (WebElement e : elems) {
                        if (StringUtils.defaultString(e.getText()).contains(text)) {
                            return e;
                        }
                    }
                } catch (StaleElementReferenceException ex) {
                    System.out.print("[StaleElementReferenceException] try again ! ");
                    _TimeSleep_(retryWait);
                }
            }
            return null;
        }

        public static List<WebElement> waitPageElementByCsss(final String css, final String text, final WebDriver driver) {
            final int retryTime = 10;
            final long retryWait = 500;
            return waitPageElementByCsss(css, text, driver, retryTime, retryWait);
        }

        public static List<WebElement> waitPageElementByCsss(final String css, final String text, final WebDriver driver, final int retryTime, final long retryWait) {
            for (int t = 0; t < retryTime; t++) {
                try {
                    new WebDriverWait(driver, WAIT_IN_SECONDS).until(//
                            new java.util.function.Function<WebDriver, Boolean>() {
                                @Override
                                public Boolean apply(WebDriver t) {
                                    List<WebElement> elems = driver.findElements(By.cssSelector(css));
                                    System.out.println("check_css_exists = " + elems.size());
                                    if (elems.isEmpty()) {
                                        return false;
                                    }
                                    return true;
                                }
                            });
                    List<WebElement> elems = driver.findElements(By.cssSelector(css));
                    List<WebElement> rtnLst = new ArrayList<WebElement>();
                    for (WebElement e : elems) {
                        if (StringUtils.defaultString(e.getText()).contains(text)) {
                            rtnLst.add(e);
                        }
                    }
                    return rtnLst;
                } catch (StaleElementReferenceException ex) {
                    System.out.print("[StaleElementReferenceException] try again ! ");
                    _TimeSleep_(retryWait);
                }
            }
            return null;
        }

        public static WebElement waitPageElementByXpath(final String xpath, final WebDriver driver) {
            final int retryTime = 10;
            final long retryWait = 500;
            return waitPageElementByXpath(xpath, driver, retryTime, retryWait);
        }

        public static WebElement waitPageElementByXpath(final String xpath, final WebDriver driver, final int retryTime, final long retryWait) {
            for (int t = 0; t < retryTime; t++) {
                try {
                    new WebDriverWait(driver, WAIT_IN_SECONDS).until(//
                            new java.util.function.Function<WebDriver, Boolean>() {
                                @Override
                                public Boolean apply(WebDriver t) {
                                    List<WebElement> elems = driver.findElements(By.xpath(xpath));
                                    System.out.println("check_xpath_exists = " + elems.size());
                                    if (elems.isEmpty()) {
                                        return false;
                                    }
                                    return true;
                                }
                            });
                    return driver.findElements(By.xpath(xpath)).get(0);
                } catch (StaleElementReferenceException ex) {
                    System.out.print("[StaleElementReferenceException] try again ! ");
                    _TimeSleep_(retryWait);
                }
            }
            return null;
        }

        public static List<WebElement> waitPageElementByXpaths(final String xpath, final WebDriver driver) {
            final int retryTime = 10;
            final long retryWait = 500;
            return waitPageElementByXpaths(xpath, driver, retryTime, retryWait);
        }

        public static List<WebElement> waitPageElementByXpaths(final String xpath, final WebDriver driver, final int retryTime, final long retryWait) {
            for (int t = 0; t < retryTime; t++) {
                try {
                    new WebDriverWait(driver, WAIT_IN_SECONDS).until(//
                            new java.util.function.Function<WebDriver, Boolean>() {
                                @Override
                                public Boolean apply(WebDriver t) {
                                    List<WebElement> elems = driver.findElements(By.xpath(xpath));
                                    System.out.println("check_xpath_exists = " + elems.size());
                                    if (elems.isEmpty()) {
                                        return false;
                                    }
                                    return true;
                                }
                            });
                    return driver.findElements(By.xpath(xpath));
                } catch (StaleElementReferenceException ex) {
                    System.out.print("[StaleElementReferenceException] try again ! ");
                    _TimeSleep_(retryWait);
                }
            }
            return null;
        }
    }
}
