package tqs.tests.PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage {
    private WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    // Locators
    @FindBy(id = "inputName")
    @CacheLookup
    private WebElement nameInput;
    
    @FindBy(id = "address")
    @CacheLookup
    private WebElement addressInput;
    
    @FindBy(id = "city")
    @CacheLookup
    private WebElement cityInput;
    
    @FindBy(id = "state")
    @CacheLookup
    private WebElement stateInput;
    
    @FindBy(id = "zipCode")
    @CacheLookup
    private WebElement zipCodeInput;
    
    @FindBy(id = "cardType")
    @CacheLookup
    private WebElement cardTypeDropdown;
    
    @FindBy(id = "creditCardNumber")
    @CacheLookup
    private WebElement creditCardNumberInput;
    
    @FindBy(id = "creditCardMonth")
    @CacheLookup
    private WebElement creditCardMonthInput;
    
    @FindBy(id = "creditCardYear")
    @CacheLookup
    private WebElement creditCardYearInput;
    
    @FindBy(id = "nameOnCard")
    @CacheLookup
    private WebElement nameOnCardInput;
    
    @FindBy(css = "input[type='submit']")
    @CacheLookup
    private WebElement purchaseFlightButton;


    public void enterName(String name) {
        nameInput.sendKeys(name);
    }

    public void enterAddress(String address) {
        addressInput.sendKeys(address);
    }

    public void enterCity(String city) {
        cityInput.sendKeys(city);
    }

    public void enterState(String state) {
        stateInput.sendKeys(state);
    }

    public void enterZipCode(String zipCode) {
        zipCodeInput.sendKeys(zipCode);
    }

    public void enterCreditCardNumber(String cardNumber) {
        creditCardNumberInput.sendKeys(cardNumber);
    }

    public void enterCreditCardMonth(String month) {
        creditCardMonthInput.sendKeys(month);
    }

    public void enterCreditCardYear(String year) {
        creditCardYearInput.sendKeys(year);
    }

    public void enterNameOnCard(String nameOnCard) {
        nameOnCardInput.sendKeys(nameOnCard);
    }

    public SucessPage clickPurchaseFlight() {
        purchaseFlightButton.click();

        return new SucessPage(driver);
    }
} 
