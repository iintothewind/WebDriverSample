package web.driver.base;


import web.driver.util.Selenium;
import web.driver.util.WebDriverFactory;

public class ChromeFunctionalTest extends FunctionalTest {
    @Override
    public Selenium createSelenium() {
        if (selenium == null) {
            System.setProperty("webdriver.chrome.driver", System.getProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe"));
            selenium = new Selenium(WebDriverFactory.createDriver(WebDriverFactory.BrowserType.chrome.name()), System.getProperty("selenium.url.base", "http://confluencesys.int.corp.sun")).blockEnhance();
            selenium.setStopAtShutdown();
            selenium.maxWindow();
        }
        return selenium;
    }
}
