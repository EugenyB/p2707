package tests;

import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.util.Properties;

public abstract class BaseTest {
    Properties properties;
    WebDriver driver;

    @BeforeClass
    @SneakyThrows
    public void beforeClass() {
        properties = new Properties();
        try(BufferedReader reader = new BufferedReader(new FileReader("config.properties"))) {
            properties.load(reader);
            File file = new File(properties.getProperty("path"));
            String driverName = properties.getProperty("driver");
            System.setProperty(driverName, file.getAbsolutePath());

            if (driverName.contains("chrome")) {
                driver = new ChromeDriver();
            } else if (driverName.contains("edge")) {
                driver = new EdgeDriver();
            } else if (driverName.contains("firefox")) {
                driver = new FirefoxDriver();
            }
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }


    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    @DataProvider(name = "findByTextData")
    public Object[][] createDataForFindByText(){
        return new Object[][]{
                {"selenium"},
                {"selenium java"},
                {"Eugeny"},
                {"Eugeny Berkunsky"}
        };
    }
}
