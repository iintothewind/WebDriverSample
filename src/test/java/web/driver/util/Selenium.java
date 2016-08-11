package web.driver.util;


import com.google.common.base.Preconditions;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import web.driver.util.Blocker.BlockEnhancer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Selenium {
    public static final int DEFAULT_WAIT_TIME = 20;
    public static final String SNAPSHOT_SAVE_DIR = "target/screenshots/";
    public static final String SNAPSHOT_FILE_EXTENSION = ".png";
    private final WebDriver driver;
    private final String baseUrl;

    public Selenium(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        setTimeout(DEFAULT_WAIT_TIME);
    }

    public Selenium(WebDriver driver) {
        this(driver, "");
    }

    public Selenium blockEnhance() {
        Preconditions.checkState(!this.getClass().getCanonicalName().contains("CGLIB"), "Target is already enhanced by CGLIB");
        return BlockEnhancer.enhance(this.getClass(), new Class[]{WebDriver.class, String.class}, new Object[]{driver, baseUrl}).create();
    }


    public void setStopAtShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread("Selenium Quit Hook") {
            @Override
            public void run() {
                quit();
            }
        });
    }

    public void open(String url) {
        final String urlToOpen = !url.contains("://") ? baseUrl + (!url.startsWith("/") ? "/" : "") + url : url;
        driver.get(urlToOpen);
    }

    public String getLocation() {
        return driver.getCurrentUrl();
    }

    public void back() {
        driver.navigate().back();
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public void quit() {
        try {
            driver.quit();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    public void maxWindow() {
        driver.manage().window().maximize();
    }

    public void close() {
        driver.close();
    }


    public void setTimeout(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isVisible(By by) {
        return driver.findElement(by).isDisplayed();
    }

    public void type(By by, String text) {
        WebElement element = driver.findElement(by);
        element.clear();
        element.sendKeys(text);
    }

    public void click(By by) {
        driver.findElement(by).click();
    }

    public void check(By by) {
        WebElement element = driver.findElement(by);
        check(element);
    }

    public void check(WebElement element) {
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void uncheck(By by) {
        WebElement element = driver.findElement(by);
        uncheck(element);
    }

    public void uncheck(WebElement element) {
        if (element.isSelected()) {
            element.click();
        }
    }

    public boolean isChecked(By by) {
        WebElement element = driver.findElement(by);
        return isChecked(element);
    }

    public boolean isChecked(WebElement element) {
        return element.isSelected();
    }

    public Select getSelect(By by) {
        return new Select(driver.findElement(by));
    }

    public String getAttribute(By by, String name) {
        return findElement(by).getAttribute(name);
    }

    public String getAttribute(WebElement element, String name) {
        return element.getAttribute(name);
    }

    public String getText(By by) {
        return driver.findElement(by).getText();
    }

    public String getValue(By by) {
        return getValue(driver.findElement(by));
    }

    public String getValue(WebElement element) {
        return element.getAttribute("value");
    }

    public void snapshot(String basePath, String outputFileName) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File targetFile = new File(basePath, outputFileName);
        try {
            FileUtils.copyFile(srcFile, targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void snapshot(String outputFileName) {
        snapshot(SNAPSHOT_SAVE_DIR, outputFileName.concat(SNAPSHOT_FILE_EXTENSION));
    }

    public void waitForTitleIs(String title) {
        waitForCondition(ExpectedConditions.titleIs(title), DEFAULT_WAIT_TIME);
    }

    public void waitForTitleIs(String title, int timeout) {
        waitForCondition(ExpectedConditions.titleIs(title), timeout);
    }

    public void waitForTitleContains(String title) {
        waitForCondition(ExpectedConditions.titleContains(title), DEFAULT_WAIT_TIME);
    }

    public void waitForTitleContains(String title, int timeout) {
        waitForCondition(ExpectedConditions.titleContains(title), timeout);
    }

    public void waitForVisible(By by) {
        waitForCondition(ExpectedConditions.visibilityOfElementLocated(by), DEFAULT_WAIT_TIME);
    }

    public void waitForVisible(By by, int timeout) {
        waitForCondition(ExpectedConditions.visibilityOfElementLocated(by), timeout);
    }

    public void waitForTextPresent(By by, String text) {
        waitForCondition(ExpectedConditions.textToBePresentInElementLocated(by, text), DEFAULT_WAIT_TIME);
    }

    public void waitForTextPresent(By by, String text, int timeout) {
        waitForCondition(ExpectedConditions.textToBePresentInElementLocated(by, text), timeout);
    }

    public void waitForValuePresent(By by, String value) {
        waitForCondition(ExpectedConditions.textToBePresentInElementValue(by, value), DEFAULT_WAIT_TIME);
    }

    public void waitForValuePresent(By by, String value, int timeout) {
        waitForCondition(ExpectedConditions.textToBePresentInElementValue(by, value), timeout);
    }

    public <T> T waitForCondition(ExpectedCondition<T> expectedCondition, int timeout) {
        try {
            return (new WebDriverWait(driver, timeout)).until(expectedCondition);
        } catch (TimeoutException e) {
            snapshot("timeout_at_" + System.currentTimeMillis());
            throw e;
        }
    }

    public boolean isTextPresent(String text) {
        String bodyText = driver.findElement(By.tagName("body")).getText();
        return bodyText.contains(text);
    }

    public String getTable(WebElement table, int rowIndex, int columnIndex) {
        return table.findElement(By.xpath("//tr[" + (rowIndex + 1) + "]//td[" + (columnIndex + 1) + "]")).getText();
    }

    public String getTable(By by, int rowIndex, int columnIndex) {
        return getTable(driver.findElement(by), rowIndex, columnIndex);
    }

    public String accept() {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    public String dismiss() {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.dismiss();
        return alertText;
    }
}