package tqs.tests;

import io.github.bonigarcia.seljup.DockerBrowser;
import static io.github.bonigarcia.seljup.BrowserType.CHROME;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import tqs.tests.PageObject.CheckoutPage;
import tqs.tests.PageObject.HomePage;
import tqs.tests.PageObject.SelectFlight;
import tqs.tests.PageObject.SucessPage;

@ExtendWith(SeleniumJupiter.class)
public class DockerChromeTest {

    @Test
    public void testMainPage(@DockerBrowser(type = CHROME) WebDriver driver) {
        HomePage hp = new HomePage(driver);
        hp.open();

        hp.selectDepartureCity("San Diego");
        hp.selectDestinationCity("Berlin");

        SelectFlight sfp = hp.clickFindFlights();

        while(!sfp.isLoaded()) {}

        CheckoutPage cop = sfp.selectFirstFlight();

        SucessPage finalPage = cop.clickPurchaseFlight();

        assertTrue(
            finalPage
            .getStatus()
            .equals("PendingCapture")
        );
    }
}
