package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainMenu {

    //Платежи
    @FindBy(css = "ul[id*='mainMenu'] > li > span > a[href*='/payments/'] > span")
    private WebElement payment;

    private final WebDriver driver;

    public MainMenu(WebDriver driver) {
        this.driver = driver;
    }

    public void chooseElement(String element) throws InterruptedException {
        switch (element) {
            case "Платежи":
                payment.click();
                break;
            default:
                throw new IllegalStateException("Указан несуществующий элемент главного меню");
        }

    }

}