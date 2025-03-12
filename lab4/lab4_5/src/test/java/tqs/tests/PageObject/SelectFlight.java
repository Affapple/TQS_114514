package tqs.tests.PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.CacheLookup;
import java.util.List;

public class SelectFlight {
    private WebDriver driver;

    public SelectFlight(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Locators
    @FindBy(css = "table tbody tr")
    private List<WebElement> flightRows;
    
    @FindBy(css = "input[type='submit']")
    @CacheLookup
    private WebElement chooseFlightButton;


    // Get number of available flights
    public int getNumberOfFlights() {
        return flightRows.size();
    }
    
    // Select the first flight
    public CheckoutPage selectFirstFlight() {
        flightRows
            .get(0)
            .findElement(
                By.cssSelector("input[type='submit']")
        ).click();

        return new CheckoutPage(driver);
    }

    public boolean isLoaded() {
        return 0 < getNumberOfFlights();
    }
}
