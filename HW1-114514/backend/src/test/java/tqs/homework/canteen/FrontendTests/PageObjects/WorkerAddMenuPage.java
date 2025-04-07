package tqs.homework.canteen.FrontendTests.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WorkerAddMenuPage extends Page {
    public WorkerAddMenuPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }

    @FindBy(id="set-date")
    private WebElement selectDate;

    @FindBy(id="set-time")
    private WebElement selectTime;

    @FindBy(id="confirm-menu")
    private WebElement confirmMenuButton;

    @FindBy(id="cancel-menu")
    private WebElement cancelMenuButton;


    @Override
    public void switchMode() {
        return;
    }
}