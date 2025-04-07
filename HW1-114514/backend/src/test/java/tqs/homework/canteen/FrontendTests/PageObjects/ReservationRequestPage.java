package tqs.homework.canteen.FrontendTests.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ReservationRequestPage extends Page {
    public ReservationRequestPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }

    @FindBy(id="close-modal")
    private WebElement closeModalButton;

    public MenusPage closeModal() {
        closeModalButton.click();
        return new MenusPage(driver, isWorker);
    }

    @Override
    public void switchMode() {
        return;
    }
}