package tqs.homework.canteen.FrontendTests.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import tqs.homework.canteen.EnumTypes.MenuTime;

public class WorkerAddMenuPage extends Page {
    public WorkerAddMenuPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }

    @Override
    public void switchMode() {
        return;
    }

    
    public void setDate(String date) {
        WebElement selectDate = driver.findElement(By.id("set-date"));
        selectDate.sendKeys(date);
    }

    public void setTime(MenuTime time) {
        WebElement selectTime = driver.findElement(By.id("set-time"));
        Select select = new Select(selectTime);
        select.selectByValue(time.getName());
    }

    public MenusPage completeMenu() {
        WebElement confirmMenuButton = driver.findElement(By.id("confirm-menu"));
        confirmMenuButton.click();
        return new MenusPage(driver, isWorker);
    }

    public MenusPage cancelMenu() {
        WebElement cancelMenuButton = driver.findElement(By.id("cancel-menu"));
        cancelMenuButton.click();
        return new MenusPage(driver, isWorker);
    }
}