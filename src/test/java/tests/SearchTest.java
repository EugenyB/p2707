package tests;

import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import util.PropertyReader;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @SneakyThrows
    @Test
    public void testLocal() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("localsite")+"index.html");
        Thread.sleep(5000);
    }

    @SneakyThrows
    @Test
    public void testSite() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("site")+"link.html");
        Thread.sleep(5000);
        System.out.println();
        assertThat(driver.findElement(By.linkText("Recommend Selenium")).getAttribute("href"))
                .isEqualTo(PropertyReader.getInstance().getProperty("site")+"index.html");
    }

    @Test
    public void testButtons() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("site")+"button.html");
        driver.findElement(By.xpath("//input[@value='Space After ']"))
                .click();
    }

    @Test
    public void testFindAndShowStats() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("url"));
        WebElement searchField = driver.findElement(By.name("q"));
        searchField.sendKeys("Selenium Java");
        JavascriptExecutor jex = (JavascriptExecutor) driver;
        jex.executeScript("var btnK = document.querySelector('input[name=btnK]'); btnK.click();");

        String text = driver.findElement(By.id("result-stats")).getText();
        System.out.println(text);
        String[] strings = text.split("\\(");
        var time = Double.parseDouble(strings[1].split(" ")[0].replaceAll(",","."));
        System.out.println("Time = " + time);
        int[] array = strings[0].chars().filter(c -> c >= '0' && c <= '9').map(c -> c - '0').toArray();
        int res = 0;
        for (int i = 0; i < array.length; i++) {
            res = res * 10 + array[i];
        }
        System.out.println("Result = "+res);
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
