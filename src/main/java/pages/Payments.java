package pages;

import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class Payments {

    //Коммунальные платежи
    @FindBy(linkText = "Коммунальные платежи")
    private WebElement flatPayment;

    //Текущий регион
    @FindBy(xpath = "//span[@class='ui-link payment-page__title_inner']")
    private WebElement currentRegion;

    //Все регионы
    @FindAll(
            @FindBy(how = How.XPATH, using = "//span[@class='ui-link']")
    )
    public List<WebElement> regions;

    //Все поставщики услуг
    @FindAll(
            @FindBy(how = How.XPATH, using = "//li[@class='ui-menu__item ui-menu__item_icons']")
    )
    public List<WebElement> paymentHolders;

    //Кнопка оплатить
    @FindBy(css = "a[href*='/zhku-moskva/oplata/']")
    private WebElement pay;

    //Кнопка узнать задолженность
    @FindBy(how = How.XPATH, using = "//button[@class='ui-button ui-button_failure ui-button_mobile-wide ui-button_provider-pay ui-button_size_xxl ui-form__button ui-button_inline']")
    private WebElement checkLoan;

    //Сообщения об ошибке
    @FindAll(
            @FindBy(how = How.XPATH, using = "//div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']")
    )
    public List<WebElement> errorMessages;

    //Код плательщика
    @FindBy(xpath = "//input[@name='provider-payerCode']")
    private WebElement payerCode;

    //Период оплаты
    @FindBy(xpath = "//input[@name='provider-period']")
    private WebElement paymentPeriod;

    //Сумма оплаты
    @FindAll(
    @FindBy(xpath = "//div[@class='ui-form__row ui-form__row_amount']/div/div/label/div/input")
    )
    private List<WebElement> paymentAmount;

    //Поле поиска поставщика услуг
    @FindBy(xpath = "//div[@class='xPwa1']/div/div/div/label/input")
    private WebElement paymentFilter;

    //Список в поле поиска поставщика услуг
    @FindAll(
    @FindBy(xpath = "//div[@class='xPwa1']/div/div/div/div/div/div")
    )
    private List<WebElement> paymentHoldersFiltered;

    //Звонок в банк, нижний элемень страницы
    @FindBy(className = "ui-online-call__text")
    private WebElement bottomElement;



    private final WebDriver driver;

    public Payments(WebDriver driver) {
        this.driver = driver;
    }

    public void chooseElement(String element){
        waitForElement(flatPayment);
        switch (element) {
            case "Коммунальные платежи":
                flatPayment.click();
                break;
            default:
                throw new IllegalStateException("Указан несуществующий элемент меню платежей");
        }

    }

    public String getCurrentRegion(){
        return currentRegion.getText();
    }

    public void openRegionMenu(){
        currentRegion.click();
    }

    public void chooseRegion(String region){
        for(int i = 0; i < regions.size(); i++) {
            if(regions.get(i).getText().equals(region)){
                regions.get(i).click();
            }
        }
    }

    public String choosePaymentHolder(){
        waitForElements(paymentHolders);
        paymentHolders.get(0).click();
        return paymentHolders.get(0).getText();
    }

    public void clickPayButton(){
        waitForElement(pay);
        pay.click();
    }

    public void checkLoan(){
        checkLoan.click();
    }

    public void fillPayerCode(String text){
        payerCode.clear();
        payerCode.sendKeys(text);
    }

    public void fillPaymentPeriod(String text){
        waitForElement(paymentPeriod);
        paymentPeriod.clear();
        paymentPeriod.sendKeys(text);
    }

    public void fillPaymentAmount(String text){
        paymentAmount.get(1).clear();
        paymentAmount.get(1).sendKeys(text);
    }

    public void fillPaymentFilter(String text) {
        waitForElement(paymentFilter);
        paymentFilter.sendKeys(text);
    }

    public void checkErrorMessage(String message){
        waitForElements(errorMessages);
        String actualMessage = errorMessages.get(0).getText();
        Assert.assertEquals("Неверное сообщение об ошибке: ", actualMessage, message);
        Assert.assertEquals("На странице больше одного сообщения об ошибке: ", 1, errorMessages.size());
    }

    public String getPaymentHolderFiltered(int index){
        waitForElements(paymentHoldersFiltered);
        String text = paymentHoldersFiltered.get(index).getText();
        paymentHoldersFiltered.get(index).click();
        return text;
    }

    public boolean searchPaymentHolder(String expectedHolder){
        boolean isExist = false;
        waitForElements(paymentHolders);
        for (WebElement paymentHolder : paymentHolders) {
            if(paymentHolder.getText().equals(expectedHolder)){
                isExist = true;
            }
        }
        return isExist;
    }

    private void waitForElement(WebElement element) {
        for(int i = 0; i < 5; i++){
            try {
                if (element.isDisplayed()) {
                    return;
                }
            } catch (NoSuchElementException e) {
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForElements(List<WebElement> elements) {
        for(int i = 0; i < 10; i++){
            try {
                if (elements.size() != 0) {
                    return;
                }
            } catch (NoSuchElementException e) {
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void scrollToBottom() throws InterruptedException {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        waitForElements(paymentHolders);
        int initialSize = paymentHolders.size();
        int actualSize;
        for(int i =0; i < 20; i++) {
            jse.executeScript("window.scrollBy(0,1000)", "");
            Thread.sleep(3000);
            actualSize = paymentHolders.size();
            if(initialSize == actualSize){
                return;
            }
            initialSize = paymentHolders.size();
        }
        throw new IllegalStateException("Достигнут лимит прокрутки страницы");
    }
}
