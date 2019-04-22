import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.MainMenu;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pages.Payments;

import java.util.concurrent.TimeUnit;

public class PaymentTest {


    @BeforeSuite
    public void initDriver() throws Exception {


    }

    @Test
    public void checkPayment() throws InterruptedException {
        System.setProperty("webdriver.gecko.driver","C:\\geckodriver\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.get("http://mdmdev.sdi-solution.ru/login");
        TimeUnit.SECONDS.sleep(10);
        driver.findElement(By.xpath("//*[contains(@id, 'email')]/parent::div")).click();
        driver.findElement(By.xpath("//*[contains(@id, 'email')]")).sendKeys("artem");
        driver.findElement(By.id("password")).sendKeys("111");
        driver.findElement(By.xpath("//*[contains(text(),'Войти')]")).click();
        driver.get("http://mdmdev.sdi-solution.ru/content/I?classifierId=4&treeItemId=-1&taxonomyReferenceId=468");
        //driver.findElement(By.xpath("//*[contains(@title,'Композиты')]")).click();
        TimeUnit.SECONDS.sleep(5);
        driver.findElement(By.xpath("(//*[contains(@class,'toggle-children')]/parent::span)[3]")).click();
        TimeUnit.SECONDS.sleep(5);
    }

    @AfterSuite
    public void quitDriver() throws Exception {
        //driver.quit();
    }
}