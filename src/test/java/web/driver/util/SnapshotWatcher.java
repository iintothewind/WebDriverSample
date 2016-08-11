package web.driver.util;


import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class SnapshotWatcher extends TestWatcher {
    private final Selenium selenium;
    private final String saveDir;

    public SnapshotWatcher(Selenium selenium) {
        this.selenium = selenium;
        this.saveDir = Selenium.SNAPSHOT_SAVE_DIR;
    }

    public SnapshotWatcher(Selenium selenium, String saveDir) {
        this.selenium = selenium;
        this.saveDir = saveDir;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        String outputFileName = description.getClassName() + "." + description.getMethodName() + ".png";
        selenium.snapshot(saveDir, outputFileName);
    }
}