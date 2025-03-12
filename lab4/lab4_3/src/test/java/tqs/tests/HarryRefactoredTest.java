package tqs.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import io.github.bonigarcia.seljup.SeleniumJupiter;

@ExtendWith(SeleniumJupiter.class)
public class HarryRefactoredTest {
  @Test
  public void harry(FirefoxDriver driver) {
    driver.get("https://cover-bookstore.onrender.com/");
    driver.manage().window().setSize(new Dimension(1132, 1090));
    driver.findElement(By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")).click();
    driver.findElement(By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")).sendKeys("Harry Potter");
    driver.findElement(By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")).sendKeys(Keys.ENTER);

    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1_000));
    
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    WebElement firstResult = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector("[data-testid=book-search-item]")
    ));
    assertTrue(
        firstResult.getText()
        .contains("Harry Potter and the Sorcerer's Stone")
    );
  }
}
