package web.driver.base;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.openqa.selenium.By;
import web.driver.util.Selenium;
import web.driver.util.SnapshotWatcher;


public abstract class FunctionalTest {
    protected Selenium selenium;
    protected String username = System.getProperty("username", "admin");
    protected String password = System.getProperty("password", "admin");

    public abstract Selenium createSelenium();

    @Rule
    public TestRule snapshotWhenError() {
        return new SnapshotWatcher(createSelenium());
    }

    public void login(String username, String password) {
        this.selenium = createSelenium();
        this.username = username;
        this.password = password;
        selenium.open("/confluence/logout.action");
        if (selenium.getTitle().contains("Log In") && !selenium.getText(By.id("action-messages")).contains("logged in as")) {
            selenium.type(By.name("os_username"), username);
            selenium.type(By.name("os_password"), password);
            selenium.check(By.name("os_cookie"));
            selenium.click(By.name("login"));
            selenium.waitForTitleContains("Dashboard");
        }
    }


}
