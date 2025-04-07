package tqs.homework.canteen.FrontendTests.PageObjects;

import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class RestaurantsPage extends Page {
    public RestaurantsPage(WebDriver driver, boolean isWorker) {
        super(driver, isWorker);
    }


    @FindBy(id = "search-reservations")
    private WebElement searchReservationsButton;

    private List<WebElement> availableRestaurants; 
    
    public void waitForRestaurants() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='_restaurant_']")));
        availableRestaurants = driver.findElements(By.cssSelector("[class^='_restaurant_']"));
    }

    public SearchReservationsPage goToSearchReservation() {
        searchReservationsButton.click();
        return new SearchReservationsPage(driver, isWorker);
    }

    public MenusPage goToMenusOfRestaurant(String restaurantName) {
        List<WebElement> filteredRestaurants = availableRestaurants
            .stream()
            .filter(restaurant -> restaurant.getText().equals(restaurantName))
            .collect(Collectors.toList());
        if (filteredRestaurants.isEmpty() || filteredRestaurants.size() > 1) {
            throw new RuntimeException("Restaurant not found");
        }
        filteredRestaurants.get(0).click();

        return new MenusPage(driver, isWorker);
    }

    public int getNumberOfRestaurants(){
        waitForRestaurants();
        return availableRestaurants.size();
    }
}
