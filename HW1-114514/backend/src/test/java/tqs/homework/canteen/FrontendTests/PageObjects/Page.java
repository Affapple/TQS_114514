package tqs.homework.canteen.FrontendTests.PageObjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Page {
    protected WebDriver driver;
    protected boolean isWorker;

    private static String url;

    public void setFrontendUrl(String url) {
        this.url = url;
    }

    public Page(WebDriver driver, boolean isWorker) {
        this.driver = driver;
        this.isWorker = isWorker;
    }

    public Page(WebDriver driver) {
        this(driver, false);
    }

    @FindBy(id="switch-mode")
    protected WebElement switchModeButton;
    
    public void switchMode() {
        switchModeButton.click();
        isWorker = !isWorker;
    }

    public void navigateToHomePage() {
        driver.get(url);
    }

    public void waitMillis(int millis) {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(millis));
    }
}