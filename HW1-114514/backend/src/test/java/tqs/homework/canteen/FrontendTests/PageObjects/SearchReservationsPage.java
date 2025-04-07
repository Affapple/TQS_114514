package tqs.homework.canteen.FrontendTests.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchReservationsPage extends Page {
    public SearchReservationsPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }
    
    @FindBy(id="reservation-code")
    private WebElement reservationCodeInput;

    @FindBy(id="search")
    private WebElement searchButton;

    @FindBy(id="close")
    private WebElement closeButton;
    


    public RestaurantsPage close() {
        closeButton.click();
        return new RestaurantsPage(driver, isWorker);
    }

    public ReservationDataPage search() {
        searchButton.click();
        return new ReservationDataPage(driver, isWorker);
    }

    @Override
    public void switchMode() {
        return;
    }
}
