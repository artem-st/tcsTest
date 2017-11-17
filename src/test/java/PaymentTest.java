import org.junit.Assert;
import pages.MainMenu;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pages.Payments;

public class PaymentTest {
    private WebDriver driver;

    @BeforeSuite
    public void initDriver() throws Exception {
        MyChromeDriver dr = new MyChromeDriver();
        driver = dr.getChromeDriver(driver);
    }

    @Test
    public void checkPayment() throws InterruptedException {
        MainMenu mainMenu = PageFactory.initElements(driver, MainMenu.class);
        mainMenu.chooseElement("Платежи");
        Payments payments = PageFactory.initElements(driver, Payments.class);
        payments.chooseElement("Коммунальные платежи");
        String currentRegion = payments.getCurrentRegion();
        if(!currentRegion.equals("г. Москва")){
            payments.chooseRegion("г. Москва");
        }
        payments.openRegionMenu();
        payments.chooseRegion("г. Москва");
        String paymentHolder = payments.choosePaymentHolder();
        payments.clickPayButton();

        payments.fillPaymentPeriod("102017");
        payments.fillPaymentAmount("100");
        payments.checkLoan();
        payments.checkErrorMessage("Поле обязательное");

        payments.fillPayerCode("123");
        payments.checkLoan();
        payments.checkErrorMessage("Поле неправильно заполнено");

        payments.fillPayerCode("0000000000");
        payments.fillPaymentPeriod("999999");
        payments.checkLoan();
        payments.checkErrorMessage("Поле заполнено некорректно");

        payments.fillPaymentPeriod("102017");
        payments.fillPaymentAmount("0");
        payments.checkLoan();
        payments.checkErrorMessage("Минимальная сумма перевода - 10 \u20BD");

        mainMenu.chooseElement("Платежи");
        payments.fillPaymentFilter("жку");
        String filteredPaymentHolder = payments.getPaymentHolderFiltered(0);
        if(!filteredPaymentHolder.toLowerCase().contains(paymentHolder.toLowerCase())){
            throw new IllegalStateException("не совпадает название поставщика услуг: " + filteredPaymentHolder + " " + paymentHolder);
        }
        payments.clickPayButton();
        payments.fillPayerCode("0000000000");
        payments.fillPaymentPeriod("102017");
        payments.fillPaymentAmount("0");
        payments.checkLoan();
        payments.checkErrorMessage("Минимальная сумма перевода - 10 \u20BD");

        mainMenu.chooseElement("Платежи");

        payments.openRegionMenu();
        payments.chooseRegion("г. Санкт-Петербург");
        payments.chooseElement("Коммунальные платежи");
        payments.scrollToBottom();
        Assert.assertFalse("На странице найден лишний поставщик: " + paymentHolder, payments.searchPaymentHolder(paymentHolder));
    }

    @AfterSuite
    public void quitDriver() throws Exception {
        driver.quit();
    }
}