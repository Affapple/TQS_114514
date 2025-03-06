/*
 * (C) Copyright 2023 Boni Garcia (https://bonigarcia.github.io/)
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
package io.github.bonigarcia.webdriver.jupiter.ch02.helloworld_selenium_manager;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;

import io.github.bonigarcia.seljup.SeleniumJupiter;

@ExtendWith(SeleniumJupiter.class)
class HelloWorldFirefoxJupiterTest {

    static final Logger log = getLogger(lookup().lookupClass());

    @Test
    void whenLoadFormPage_thenFormExists(FirefoxDriver driver) {
        // Exercise
        String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(sutUrl);

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        var form = driver.findElement(By.tagName("form"));
        assertThat(form).isNotNull();
    }

    @Test
    void whenWriteInTextArea_thenTextIsSaved(FirefoxDriver driver) {
        // Exercise
        String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(sutUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        var textInput = driver.findElement(By.id("my-text-id"));
        textInput.click();

        String text = "Write write";
        Actions actions = new Actions(driver);
        actions.sendKeys(textInput, text).build().perform();

        assertThat(textInput.getDomProperty("value")).isEqualTo(text);
    }
    
    @Test
    void whenWriteInReadonly_thenNoTextIsEdited(FirefoxDriver driver) {
        // Exercise
        String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(sutUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        Actions actions = new Actions(driver);

        var textInput = driver.findElement(By.name("my-readonly"));
        String initialText = textInput.getText();
        textInput.click();

        String text = "Write write";
        actions.sendKeys(textInput, text).build().perform();

        assertThat(textInput.getText()).isEqualTo(initialText);
    }

    @Test
    void whenSubmit_thenShowSubmitSuccessfull(FirefoxDriver driver) {
        // Exercise
        String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(sutUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        var button = driver.findElement(By.tagName("button"));
        button.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        assertThat(driver.getCurrentUrl())
            .startsWith(
                "https://bonigarcia.dev/selenium-webdriver-java/submitted-form.html"
            );
    }
}
