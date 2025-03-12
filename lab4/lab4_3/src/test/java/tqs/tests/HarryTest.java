package tqs.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import io.github.bonigarcia.seljup.SeleniumJupiter;

@ExtendWith(SeleniumJupiter.class)
public class HarryTest {
  @Test
  public void harry(FirefoxDriver driver) {
    driver.get("https://cover-bookstore.onrender.com/");
    driver.manage().window().setSize(new Dimension(1132, 1090));
    driver.findElement(By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")).click();
    driver.findElement(By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")).sendKeys("Harry Potter");
    driver.findElement(By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")).sendKeys(Keys.ENTER);

    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1_000));
    assertEquals(
        driver.findElement(By.cssSelector(".SearchList_bookTitle__1wo4a")).getText(), 
        "Harry Potter and the Sorcerer\'s Stone"
    );
  }
}
