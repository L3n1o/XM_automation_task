package frontend.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AssetPage extends BasePage {

    // Page controls immutable
    private final By pageHeader = By.xpath("//h1[@class='ltr text-left']");
    private final By tradingConditionsHeader = By.xpath("//h2[text()='Trading Conditions']");
    // Page controls mutable
    private final String assetValueLocator = "//tr[.//*[text()='%s']]/td[2]/span";
    private final String spreadAsLowLocator = "//tr[@class='hidden-xs'][.//*[text()='%s']]/td[2]/span";

    // Constructor
    public AssetPage(WebDriver driver) {
        super(driver);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElement(pageHeader));
    }

    // Page methods
    @Step("Scroll page to Trading Conditions")
    public void scrollPageToTradingConditions()
    {
        try {
            WebElement element = driver.findElement(tradingConditionsHeader);
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

    @Step("Get data from table")
    public String getDataFromTable(String assetName) {
        try {
            String assetValue = driver.findElement(By.xpath(String.format(assetValueLocator, assetName))).getText();
            // bottom required to read Spread as low as (quote currency) value, because it's listed in both tables (why?) -> should be considered as bug
            if (assetValue.length() < 1) // can be changed to (assetName == "Spread as low as (quote currency)")
            {
                assetValue = driver.findElement(By.xpath(String.format(spreadAsLowLocator, assetName))).getText();
            }
            return assetValue;
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
