package tqs.homework.canteen.FrontendTests.PageObjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    protected WebElement switchModeButton;
    
    public void switchMode() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("switch-mode")));
        } catch (TimeoutException e) {}
        switchModeButton = driver.findElement(By.id("switch-mode"));
        switchModeButton.click();
        isWorker = !isWorker;
    }

    public void navigateToHomePage() {
        driver.get(url);
    }

    public void waitMillis(int millis) {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(millis));
    }

    public boolean isWeatherDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='_weatherInfo_']")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
        if (isWorker) {
            switchMode();
        }
    }
}