package tqs.homework.canteen.FrontendTests.PageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WorkerAllReservationsPage extends Page {
    public WorkerAllReservationsPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
        waitForReservations();
    }

    private List<WebElement> reservations;

    private void waitForReservations() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='_reservationItem_']")));
        reservations = driver.findElements(By.cssSelector("[class^='_reservationItem_']"));
    }

    public void cancelReservation(String reservationCode) {
        WebElement reservation = findReservation(reservationCode);
        reservation.findElement(By.cssSelector("[class^='_cancelButton_']")).click();
    }

    public void checkinReservation(String reservationCode) {
        WebElement reservation = findReservation(reservationCode);
        reservation.findElement(By.cssSelector("[class^='_confirmButton_']")).click();
    }

    @Override
    public void switchMode() {
        return;
    }

    public int getNumberOfReservations() {
        return reservations.size();
    }

    public String getStatusOfReservation(String reservationCode) {
        WebElement reservation = findReservation(reservationCode);
        return reservation.findElement(By.cssSelector("[class^='_reservationStatus_']")).getText();
    }

    public WebElement findReservation(String reservationCode) {
        WebElement reservation;
        
        // If no code is provided, select the first reservation that is not checked in
        if(reservationCode == null || reservationCode.isEmpty()) {
            reservation = reservations.stream().filter(
                r ->  r.findElement(
                    By.cssSelector("[class^='_reservationStatus_']")
                ).getText().equals("ACTIVE")
            ).findFirst().orElseThrow(() -> new RuntimeException("Reservation not found"));
        } else {
            reservation = reservations.stream().filter(
                r ->  r.findElement(
                    By.cssSelector("[class^='_reservationCode_']")
                ).getText().contains(reservationCode)
            ).findFirst().orElseThrow(() -> new RuntimeException("Reservation not found"));
        }
        return reservation;
    }

    @Override
    public void refreshPage() {
        super.refreshPage();
        super.switchMode();
        waitForReservations(); 
    }
}