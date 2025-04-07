package tqs.homework.canteen.FrontendTests.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ReservationDataPage extends Page {
    public ReservationDataPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }

    @FindBy(id="close")
    private WebElement closeButton;

    @FindBy(id="reservation-status")
    private WebElement reservationStatus;


    @FindBy(id="cancel-reservation")
    private WebElement cancelReservationButton;

    public RestaurantsPage close() {
        closeButton.click();
        return new RestaurantsPage(driver, isWorker);
    }

    public String getReservationStatus() {
        return reservationStatus.getText();
    }

    public void cancelReservation() {
        cancelReservationButton.click();
    }

    @Override
    public void switchMode() {
        return;
    }
}
