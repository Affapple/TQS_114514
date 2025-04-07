package tqs.homework.canteen.FrontendTests.PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WorkerAllReservationsPage extends Page {
    public WorkerAllReservationsPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }


    @FindBy(css="[class^='_reservationItem']")
    private List<WebElement> reservations;

    public ReservationDataPage cancelReservation(String reservationCode) {
        WebElement cancelButton;
        
        // If no code is provided, select the first reservation that is not checked in
        if(reservationCode == null || reservationCode.isEmpty()) {
            cancelButton = reservations.stream().filter(
                r ->  r.findElement(
                    By.cssSelector("[class^='_reservationStatus']")
                ).getText().equals("ACTIVE")
            ).findFirst().orElseThrow(() -> new RuntimeException("Reservation not found"));
        } else {
            cancelButton = reservations.stream().filter(
                r ->  r.findElement(
                    By.cssSelector("[class^='_reservationCode']")
                ).getText().equals(reservationCode)
            ).findFirst().orElseThrow(() -> new RuntimeException("Reservation not found"));
        }
        
        cancelButton.findElement(By.cssSelector("[class^='_cancelButton']")).click();

        return new ReservationDataPage(driver, isWorker);
    }

    public ReservationDataPage checkinReservation(String reservationCode) {
        WebElement checkinButton;
        
        // If no code is provided, select the first reservation that is not checked in
        if(reservationCode == null || reservationCode.isEmpty()) {
            checkinButton = reservations.stream().filter(
                r ->  r.findElement(
                    By.cssSelector("[class^='_reservationStatus']")
                ).getText().equals("ACTIVE")
            ).findFirst().orElseThrow(() -> new RuntimeException("Reservation not found"));
        } else {
            checkinButton = reservations.stream().filter(
                r ->  r.findElement(
                    By.cssSelector("[class^='_reservationCode']")
                ).getText().equals(reservationCode)
            ).findFirst().orElseThrow(() -> new RuntimeException("Reservation not found"));
        }
        
        checkinButton.findElement(By.cssSelector("[class^='_confirmButton']")).click();
        
        return new ReservationDataPage(driver, isWorker);
    }

    @Override
    public void switchMode() {
        return;
    }
}