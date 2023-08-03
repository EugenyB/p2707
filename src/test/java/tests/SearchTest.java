package tests;

import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import util.PropertyReader;


import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.DURATION;
import static org.testng.Assert.assertTrue;

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
        WebElement btn = driver.findElement(By.id("choose_selenium_btn"));
        assertThat(btn.isEnabled()).isTrue();
        driver.findElement(By.linkText("Disable")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        wait.withTimeout(Duration.ofMillis(500));
        assertThat(btn.isEnabled()).isFalse();
        driver.findElement(By.linkText("Enable")).click();
        wait.withTimeout(Duration.ofMillis(500));
        assertThat(btn.isEnabled()).isTrue();
    }

    @SneakyThrows
    @Test
    public void testTextFields() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("site") + "text_field.html");
        WebElement userField = driver.findElement(By.name("username"));
        userField.clear();
        userField.sendKeys("Eugeny");
        driver.findElement(By.id("pass")).sendKeys("12345678");

        driver.findElement(By.id("comments")).sendKeys("First line\nSecond line");

        assertThat(userField.getAttribute("value")).isEqualToIgnoringCase("eugeny");
        Thread.sleep(5000);
    }

    @SneakyThrows
    @Test
    public void testReadOnlyAndDisabled() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("site") + "text_field.html");
//        driver.findElement(By.name("readonly_text")).sendKeys("new value");
//        //System.out.println(driver.findElement(By.name("readonly_text")).getAttribute("value"));
//        assertThat(driver.findElement(By.name("readonly_text")).getAttribute("value")).isEqualTo("new value");
        ((JavascriptExecutor) driver).executeScript("$('#readonly_text').val('bypass');");
        assertThat(driver.findElement(By.id("readonly_text")).getAttribute("value")).isEqualTo("bypass");
        ((JavascriptExecutor) driver).executeScript("$('#disabled_text').val('anyuse');");
        Thread.sleep(5000);
    }

    @SneakyThrows
    @Test
    public void testRadioButtons() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("site") + "radio_button.html");
        Thread.sleep(1000);
//        driver.findElement(By.xpath("//input[@name='gender' and@value='female']")).click();
//        Thread.sleep(2000);
//        driver.findElement(By.xpath("//input[@name='gender' and@value='male']")).click();
//        Thread.sleep(2000);
        List<WebElement> radioButtons = driver.findElements(By.name("gender"));

//        radioButtons.stream().filter(e->e.getAttribute("value").equals("female")).findFirst().get().click();
        radioButtons.stream().filter(rb -> rb.getAttribute("value").equals("female")).forEach(WebElement::click);
        Thread.sleep(2000);

//        radioButtons.get(1).click();
//        Thread.sleep(2000);
//        radioButtons.get(0).click();
//        Thread.sleep(2000);
    }

    @SneakyThrows
    @Test
    public void testCheckBoxes() {
        driver.navigate().to(PropertyReader.getInstance().getProperty("site") + "checkbox.html");
        driver.findElement(By.name("vehicle_bike")).click();
        Thread.sleep(2000);

        WebElement element = driver.findElement(By.id("checkbox_car"));
        if (!element.isSelected()) {
            element.click();
        }
        Thread.sleep(2000);
//        assertThat(element.isSelected()).isTrue();
        assertTrue(element.isSelected());
    }

    @SneakyThrows
    @Test
    public void testSelectList() {
        //driver.navigate().to(PropertyReader.getInstance().getProperty("site") + "select_list.html");
        visit("select_list.html");
        Thread.sleep(2000);
        driver.manage().window().minimize();
        Thread.sleep(2000);
        driver.manage().window().maximize();
//        WebElement element = driver.findElement(By.name("car_make"));
//        Select select = new Select(element);
//        select.selectByVisibleText("Volvo (Sweden)");
//        Thread.sleep(2000);
//        select.selectByValue("audi");
//        Thread.sleep(2000);

        Select select = new Select(driver.findElement(By.name("test_framework")));
        select.selectByVisibleText("Selenium");
        select.selectByValue("rwebspec");
        select.selectByIndex(2);
        List<WebElement> options = select.getAllSelectedOptions();
        assertThat(options.stream().anyMatch(e->e.getAttribute("value").equals("selenium"))).isTrue();
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

    public void visit(String path) {
        driver.navigate().to(PropertyReader.getInstance().getProperty("site") + path);
    }
}
