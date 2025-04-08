package tqs.homework.canteen.FrontendTests.PageObjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReservationDataPage extends Page {
    public ReservationDataPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
        waitForSearchResult();
    }

    private void waitForSearchResult() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='_reservationCard_']")));

        reservationStatus = driver.findElement(By.id("reservation-status"));
        cancelReservationButton = driver.findElement(By.id("cancel-reservation"));
        reservationCode = driver.findElement(By.id("reservation-code"));
    }

    private WebElement reservationStatus;
    private WebElement cancelReservationButton;
    private WebElement reservationCode;

    public RestaurantsPage close() {
        WebElement closeButton = driver.findElement(By.id("close"));
        closeButton.click();
        return new RestaurantsPage(driver, isWorker);
    }

    public String getReservationStatus() {
        return reservationStatus.getText();
    }

    public String getReservationCode() {
        return reservationCode.getText();
    }

    public void cancelReservation() {
        cancelReservationButton.click();
    }

    @Override
    public void switchMode() {
        return;
    }
}
