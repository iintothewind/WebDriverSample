package web.driver.sample;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.openqa.selenium.By;
import web.driver.util.Selenium;
import web.driver.util.SnapshotWatcher;
import web.driver.util.WebDriverFactory;


public class WebDriverTest {
    private static Selenium selenium;
    private Logger log = LogManager.getLogger();
    @Rule
    public TestRule snapshotWatch = new SnapshotWatcher(selenium);


    @BeforeClass
    public static void beforeClass() {
        System.setProperty("webdriver.firefox.bin", "/usr/bin/firefox");
//        driver = new HtmlUnitDriver() {
//            @Override
//            protected WebClient modifyWebClient(WebClient client) {
//                DefaultCredentialsProvider credential = new DefaultCredentialsProvider();
//                credential.addCredentials("username", "password);
//                client.setCredentialsProvider(credential);
//                return client;
//            }
//        };
//        driver.setProxy("proxyName", 89);
        selenium = new Selenium(WebDriverFactory.createDriver("firefox"), "http://baidu.com").blockEnhance();
        selenium.setStopAtShutdown();
        selenium.maxWindow();
    }

    @AfterClass
    public static void afterClass() {
        selenium.quit();
    }

    @Test
    public void testSelenium() {
        selenium.open("/");
        System.out.println(selenium.getTitle());
        selenium.type(By.name("wd"), "iintothewind");
        selenium.check(By.id("su"));
        System.out.println(selenium.getText(By.xpath("//*[@id=\"container\"]/div[2]/div/div[2]")));
    }
}
