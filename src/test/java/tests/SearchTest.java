package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest extends BaseTest {

    @Test
    public void testOpenGoogleCom() {
        assertThat(driver).isNotNull();
        driver.navigate().to("https://www.google.com");
        WebElement searchField = driver.findElement(By.name("q"));
        searchField.click();
        assertThat(driver.getTitle()).isEqualTo("Google");
    }

    @Test(dataProvider = "findByTextData")
    public void testFindByText(String text) {
        driver.navigate().to("https://www.google.com");
        WebElement searchField = driver.findElement(By.name("q"));
        searchField.sendKeys(text);
//        WebElement btnK = driver.findElement(By.name("btnK"));
//        btnK.click();
        JavascriptExecutor jex = (JavascriptExecutor) driver;
        jex.executeScript("var btnK = document.querySelector('input[name=btnK]'); btnK.click();");
        System.out.println(driver.getTitle());
    }

    @Test
    public void testFindAndShowStats() {
        driver.navigate().to("https://www.google.com");
        WebElement searchField = driver.findElement(By.name("q"));
        searchField.sendKeys("selenium java");
        JavascriptExecutor jex = (JavascriptExecutor) driver;
        jex.executeScript("var btnK = document.querySelector('input[name=btnK]'); btnK.click();");

        String text = driver.findElement(By.id("result-stats")).getText();
        System.out.println(text);
    }

    @Test
    public void testGmail() {
        driver.navigate().to("https://www.google.com");
        WebElement gmail = driver.findElement(By.partialLinkText("чта"));
        gmail.click();
    }

    @Test
    public void testListItems() {
        driver.navigate().to("http://www.berkut.mk.ua/download/site/");
        List<WebElement> list = driver.findElements(By.tagName("a"));
        assertThat(list.stream()
                .map(WebElement::getText)
                .anyMatch(t->t.contains("button"))
        ).isTrue();

       // assertThat(list.get(2).getText()).isEqualToIgnoringCase("text field");

        System.out.println(list.get(3).getAttribute("href"));
    }
}
