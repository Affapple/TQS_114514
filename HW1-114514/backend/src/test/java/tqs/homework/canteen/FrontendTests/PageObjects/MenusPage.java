package tqs.homework.canteen.FrontendTests.PageObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import tqs.homework.canteen.EnumTypes.MenuTime;

public class MenusPage extends Page {
    public MenusPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }

    private List<WebElement> menus;
    private void waitForMenus() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='_menuCard_']")));
            menus = driver.findElements(By.cssSelector("[class^='_menuCard_']"));
        } catch (TimeoutException e) {
            menus = new ArrayList<>();
        }
    }

    public ReservationRequestPage reserveMeal(String Date, MenuTime time) {
        waitForMenus();
        WebElement menu = menus.stream().filter(
            (m) -> {
                return (
                    m.findElement(By.cssSelector("[class^='_menuDate_']")).getAttribute("value").equals(Date) 
                    && m.findElement(By.cssSelector("[class^='_menuTime_']")).getAttribute("value").equals(time.getName())
                );
            }
        ).findFirst().orElseThrow(() -> new RuntimeException("Menu not found"));

        WebElement button = menu.findElement(By.cssSelector("button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        return new ReservationRequestPage(driver, isWorker);
    }

    public WorkerAddMenuPage addMenu() {
        if (!isWorker) {
            throw new UnsupportedOperationException("Cannot add menu on non-worker mode");
        }
        driver.findElement(By.id("add-menu")).click();
        return new WorkerAddMenuPage(driver, isWorker);
    }

    public WorkerAllReservationsPage viewReservations() {
        if (!isWorker) {
            throw new UnsupportedOperationException("Cannot view reservations on non-worker mode");
        }
        driver.findElement(By.id("view-reservations")).click();
        return new WorkerAllReservationsPage(driver, isWorker);
    }

    public int getNumberOfMeals() {
        waitForMenus();
        return menus.size();
    }

    public boolean hasMenuAddOption() {
        return 0 < driver.findElements(By.id("add-menu")).size();
    }
}