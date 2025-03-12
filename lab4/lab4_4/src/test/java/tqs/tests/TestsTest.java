package tqs.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import tqs.tests.PageObject.*;

@ExtendWith(SeleniumJupiter.class)
public class TestsTest {

    @Test
    public void testMainPage(FirefoxDriver driver) {
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
