package tqs.tests.PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SucessPage {
    private WebDriver driver;

    public SucessPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath="/html/body/div[2]/div/table/tbody/tr[2]/td[2]")
    private WebElement status;


    public String getStatus() {
        return status.getText();
    }
}

