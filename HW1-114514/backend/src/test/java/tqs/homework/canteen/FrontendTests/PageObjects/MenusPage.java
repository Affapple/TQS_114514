package tqs.homework.canteen.FrontendTests.PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import tqs.homework.canteen.EnumTypes.MenuTime;

public class MenusPage extends Page {
    public MenusPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }


    @FindBy(css="[class^='_menuCard']")
    private List<WebElement> menus;

    public ReservationRequestPage reserveMeal(String Date, MenuTime time) {
        WebElement menu = menus.stream().filter(
            (m) -> {
                return (
                    m.getAttribute("value").equals(Date) 
                    && m.getAttribute("time").equals(time.getName())
                );
            }
        ).findFirst().orElseThrow(() -> new RuntimeException("Menu not found"));

        menu.findElement(By.cssSelector("button")).click();
        return new ReservationRequestPage(driver, isWorker);
    }

    public WorkerAddMenuPage addMenu() {
        if (!isWorker) {
            throw new UnsupportedOperationException("Cannot add menu on non-worker mode");
        }
        driver.findElement(By.id("add-menu")).click();
        return new WorkerAddMenuPage(driver, isWorker);
    }

    public SearchReservationsPage viewReservations() {
        if (!isWorker) {
            throw new UnsupportedOperationException("Cannot view reservations on non-worker mode");
        }
        driver.findElement(By.id("view-reservations")).click();
        return new SearchReservationsPage(driver, isWorker);
    }
}