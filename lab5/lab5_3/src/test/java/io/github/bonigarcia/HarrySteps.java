package io.github.bonigarcia;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;

public class HarrySteps {

    private WebDriver driver;
    @Given("the user is on the homepage of the online library")
    public void givenTheUserIsOnTheHomepage() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.get("https://cover-bookstore.onrender.com/");
    }
    @After
    public void closeUp() {
        driver.close();
    }

    @When("the user enters {string} into the search bar")
    public void whenTheUserEntersBookName(String bookName) {
        WebElement searchBar = driver.findElement(
            By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")
        );
        searchBar.sendKeys(bookName);
    }

    @When("the user clicks on the search button")
    public void whenTheUserClicksOnSearchButton() {
        WebElement searchButton = driver.findElement(
            By.cssSelector(
                ".Navbar_searchBtnIcon__25k0u"
            )
        );
        searchButton.click();
    }
    
    @When("the user selects {string} from the genres")
    public void whenTheUserSelectsGenre(String genre) {
        List<WebElement> genres = driver.findElements(
            By.cssSelector(".Navbar_genreLinks__vbST4")
        );

        for (var genreEle : genres) {
            if (genreEle.getText().equals(genre)) {
                genreEle.click();
                break;
            }
        }
    }


    @Then("the user should see {string} in the search results")
    public void thenTheUserShouldSeeBookInResults(String bookName) {
        List<WebElement> result = driver.findElements(
            By.cssSelector(".SearchList_bookTitle__1wo4a")
        );

        assertThat(result).extracting(WebElement::getText).containsAnyOf(bookName);
    }

    @Then("the user should see books written by {string} in the search results")
    public void thenTheUserShouldSeeBooksByAuthor(String author) {
        List<WebElement> result = driver.findElements(
            By.cssSelector(".SearchList_bookAuthor__3giPc")
        );
        assertThat(result).extracting(WebElement::getText).containsAnyOf(author);
    }

    
    @Then("the user should see the book {string} in the book list")
    public void thenTheUserShouldSeeBooksFromGenre(String bookTitle) {
        List<WebElement> result = driver.findElements(
            By.cssSelector(".GenreBooksPage_bookTitle__1OmLm")
        );

        assertThat(result).extracting(WebElement::getText).containsAnyOf(bookTitle);
    }

    @Then("the user should see no books")
    public void thenTheUserShouldSeeNoResultsMessage() {
        List<WebElement> results = driver.findElements(
            By.cssSelector(".GenreBooksPage_bookTitle__1OmLm")
        );

        assertThat(results).isEmpty();
    }
}

