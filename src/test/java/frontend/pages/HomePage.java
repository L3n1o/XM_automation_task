package frontend.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    // Cookie modal popup controls
    private final By cookieModal = By.id("cookieModal");
    private final By cookieAcceptButton = By.xpath("//*[@id='cookieModal']//button[text()='ACCEPT ALL']");
    // Page controls immutable
    private final By logo = By.xpath("//*[@class='navbar-brand logo']");
    // Page controls mutable
    private final String mainMenuButton = "//li[@class='main_nav_%s']";
    private final String mainMenuPopup = "//div[@class='box hidden-xs hidden-sm']//span[text()='%s']";
    private final String tradingMenuButton = "//li[@class='menu-%s']/a";

    private final String leftBarMenuButton = "//*[@id='main-nav']//span[text()='%s']";
    private final String leftBarTradingButton = "//*[@id='main-nav']//a[@class='navbar-nav__toggleArrow']/span[text()='Trading']";
    private final String leftBarTradingMenuButton = "//*[@id='tradingMenu']//span[contains(text(), '%s')]";

    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);
        navigateTo();
        acceptCookies();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElement(logo));
    }

    // Page methods
    @Step("Accept cookies modal if present")
    public void acceptCookies()
    {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> d.findElement(cookieModal));
            if(driver.findElement(cookieModal).isDisplayed()){
                driver.findElement(cookieAcceptButton).click();
            }
        }catch (TimeoutException e)
        {
            e.printStackTrace();
        }
    }

    @Step("Click main menu button")
    public void clickMainMenuButton(String buttonText)
    {
        try {
            if (driver.findElement(By.xpath(String.format(mainMenuButton, buttonText))).isDisplayed())
                driver.findElement(By.xpath(String.format(mainMenuButton, buttonText))).click();
            else
            {
                driver.findElement(By.xpath(String.format(leftBarMenuButton, "Menu"))).click();
                Thread.sleep(3000); //required for footer bar to disappear
                driver.findElement(By.xpath(leftBarTradingButton)).click();
            }
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Main menu popup displayed")
    public boolean mainMenuPopupDisplayed(String popupText)
    {
        return driver.findElement(By.xpath(String.format(mainMenuPopup, popupText))).isDisplayed();
    }

    @Step("Click trading menu button")
    public void clickTradingMenuButton(String buttonText)
    {
        try {
            if (driver.findElement(By.xpath(String.format(tradingMenuButton, buttonText))).isDisplayed())
                driver.findElement(By.xpath(String.format(tradingMenuButton, buttonText))).click();
            else
            {

                By menuButton = By.xpath(String.format(leftBarTradingMenuButton, buttonText.substring(0, 1).toUpperCase() + buttonText.substring(1)));
                WebElement element = driver.findElement(menuButton);
                Actions actions = new Actions(driver);
                actions.moveToElement(element);
                actions.perform();
                driver.findElement(menuButton).click();
            }
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

}
