/*
 * (C) Copyright 2017 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;

import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LibrarySteps {

    static final Logger log = getLogger(lookup().lookupClass());
    private Library lib = new Library();
    private List<Book> searchResult = new ArrayList<>();

    public LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(
            date+" 00:00",
            DateTimeFormatter.ofPattern(
                "dd MMMM yyyy HH:mm",
                Locale.ENGLISH
            )
        );
    }

    @ParameterType("book with the title '(.*)', written by '(.*)', published in (.*)")
    public Book book(String title, String author, String published) {
        LocalDateTime date = parseDate(published);
        return new Book(author, title, date);
    }

    @Given("a {book}")
    @Given("another {book}")
    public void setup(Book book) {
        lib.addBook(book);
    }


    @When("the customer searches for books published between {int} and {int}")
    public void search(Integer from, Integer to) {
        LocalDateTime fromDate = LocalDateTime.parse(
            "01-01-" + from.toString() + " 00:00",
             DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        );

        LocalDateTime toDate = LocalDateTime.parse(
            "31-12-" + to.toString() + " 23:59",
             DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        );

        searchResult = lib.findBooks(fromDate, toDate);
    }

    @Then("{int} books should have been found")
    public void verifySearchSize(int expectedSize){
        assertEquals(expectedSize, searchResult.size(), "Search result does not match in size");
    }

    @Then("^Book ([0-9]*) should have the title '(.*)'$")
    public void verifySearchTitle(int index, String title) {
        assertEquals(
            title, searchResult.get(index-1).getTitle(),
            "Search result does not match in size"
        );
    }

    /**
     * load a data table from the feature (tabular format) and call this method
     * for each row in the table. Injected parameter is a map with column name --> value
    */
    @DataTableType
    public Book bookEntry(Map<String, String> tableEntry){
    	return new Book(
    			tableEntry.get("author"),
    			tableEntry.get("title"),
    			LocalDateTime.parse(
                    tableEntry.get("published") + " 00:00",
                    DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy HH:mm"
                    )
                ) 
        );
    }

    @Given("the library is initialized with the following data")
    public void libraryIsInitializedWithBooks(List<Book> books) {
        books.forEach((book) -> lib.addBook(book));
    }

    @When("the customer searches for books published by {string}")
    public void searchByAuthor(String author) {
        searchResult = lib.findBooksByAuthor(author);
    }
}
