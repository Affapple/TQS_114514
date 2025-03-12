package tqs.tests.PageObject;

import org.openqa.selenium.support.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class HomePage {
    private WebDriver driver;
    private static String PAGE_URL="https://blazedemo.com/";

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.NAME, using= "fromPort")
    @CacheLookup
    private WebElement departureDropdown;
    
    @FindBy(how = How.NAME, using= "toPort")
    @CacheLookup
    private WebElement destinationDropdown;
    
    @FindBy(how = How.CSS, using= "input[type='submit']")
    @CacheLookup
    private WebElement findFlightsButton;
    

    public void open() {
        driver.get(PAGE_URL);
    }

    public void selectDepartureCity(String city) {
        Select dropdown = new Select(departureDropdown);
        dropdown.selectByVisibleText(city);
    }
    
    public void selectDestinationCity(String city) {
        Select dropdown = new Select(destinationDropdown);
        dropdown.selectByVisibleText(city);
    }
    
    public SelectFlight clickFindFlights() {
        findFlightsButton.click();
        return new SelectFlight(driver);
    }
}
