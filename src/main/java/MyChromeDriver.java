import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;


public class MyChromeDriver {

    public WebDriver getChromeDriver(WebDriver driver){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        String chrome = getClass().getResource("chromedriver.exe").toString().substring(6);
        System.setProperty("webdriver.chrome.driver", chrome);
        driver = new ChromeDriver(options);
        driver.navigate().to("https://www.tinkoff.ru/");
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        return driver;
    }
}