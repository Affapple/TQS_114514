package tqs.homework.canteen.FrontendTests.PageObjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchReservationsPage extends Page {
    public SearchReservationsPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
        waitForModal();
    }
    
    private void waitForModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='_modal_']")));
    }

    public RestaurantsPage close() {
        WebElement closeButton = driver.findElement(By.cssSelector("[class^='_close_']"));
        closeButton.click();
        return new RestaurantsPage(driver, isWorker);
    }

    public ReservationDataPage search(String code) {
        WebElement reservationCodeInput = driver.findElement(By.id("reservation-code-input"));
        WebElement searchButton = driver.findElement(By.id("search"));

        reservationCodeInput.sendKeys(code);
        searchButton.click();
        return new ReservationDataPage(driver, isWorker);
    }

    @Override
    public void switchMode() {
        return;
    }
}
