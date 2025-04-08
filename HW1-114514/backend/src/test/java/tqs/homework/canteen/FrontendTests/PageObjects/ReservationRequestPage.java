package tqs.homework.canteen.FrontendTests.PageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReservationRequestPage extends Page {
    public ReservationRequestPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
        waitForModal();
    }

    private void waitForModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='_modal_']")));
    }

    public MenusPage closeModal() {
        driver.findElement(By.id("close-modal")).click();
        return new MenusPage(driver, isWorker);
    }

    @Override
    public void switchMode() {
        return;
    }

    public boolean isSucessfulReservation() {
        List<WebElement> successMessages = driver.findElements(By.cssSelector("[class^='_successMessage_']"));
        return 0 < successMessages.size();
    }

    public String getReservationCode() {
        return driver.findElement(By.cssSelector("[class^='_reservationCode_']")).getText();
    }
}