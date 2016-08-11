package web.driver.base;


import web.driver.util.Selenium;
import web.driver.util.WebDriverFactory;

public class FirefoxFunctionalTest extends FunctionalTest {

    @Override
    public Selenium createSelenium() {
        if (selenium == null) {
            System.setProperty("webdriver.firefox.bin", System.getProperty("webdriver.firefox.bin", "/usr/bin/firefox"));
            selenium = new Selenium(WebDriverFactory.createDriver(WebDriverFactory.BrowserType.firefox.name()), System.getProperty("selenium.url.base", "http://confluencesys.int.corp.sun")).blockEnhance();
            selenium.setStopAtShutdown();
            selenium.maxWindow();
        }
        return selenium;
    }


}
