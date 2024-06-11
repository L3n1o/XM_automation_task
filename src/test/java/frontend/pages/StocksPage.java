package frontend.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StocksPage extends BasePage {

    // Page controls immutable
    private final By pageHeader = By.xpath("//h1[@class='ltr text-left']");
    private final By countryTable = By.id("tabContent");
    private final By stickyBar = By.xpath("//div[@class='sticky-bar active']");
    private final By stickyBarCloseButton = By.xpath("//div[@class='sticky-bar active']//i[@class='fa fa-times']");
    private final By bottomOfTable = By.xpath("//div[@class='col-md-12']/p");//div[@class='dis60']");
    private final By nextButtonDisabled = By.xpath("//a[@class='paginate_button next disabled']");
    private final By nextButtonId = By.id("DataTables_Table_0_next");
    // Page controls mutable
    private final String countryButton = "//button[@data-value='%s']";
    private final String assetNameInTable = "//td[@data-xm-qa-name='symbolWithDescription'][text()='%s']";
    private final String dataLocatorInTable = "//*[@id='DataTables_Table_0']//tr[.//td[text()='%s']]/td[%d]";
    private final String dorpdownDataLocatorInTable = "//*[@id='DataTables_Table_0']//tr[.//td[text()='%s']]/following-sibling::tr//li[@data-dtr-index=%d]/span[@class='dtr-data']";
    private final String tableAssetReadMoreButton = "//*[@id='DataTables_Table_0']//tr[.//td[text()='%s']]/td[%d]";
    private final String assetReadMoreButtonDropdown = "//*[@id='DataTables_Table_0']//tr[.//td[text()='%s']]/following-sibling::tr//a";
    // Constructor
    public StocksPage(WebDriver driver) {
        super(driver);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElement(pageHeader));
    }

    // Page methods
    @Step("Scroll page to country list")
    public void scrollPageToCountryList()
    {
        try {
            WebElement element = driver.findElement(countryTable);
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

    @Step("Close sticky bar")
    public void closeStickyBarIfPresent()
    {
        try {
            if (!driver.findElements(stickyBar).isEmpty())
            {
                driver.findElement(stickyBarCloseButton).click();
            }
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

    @Step("Click country button")
    public void clickCountryButton(String buttonText)
    {
        try {
            driver.findElement(By.xpath(String.format(countryButton, buttonText))).click();
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

    @Step("Scroll page to table")
    public void scrollPageToTable()
    {
        try {
            WebElement element = driver.findElement(bottomOfTable);
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

    @Step("Find instrument in table")
    public void findInstrumentInTable(String assetName) {
        try {
            while (driver.findElements(By.xpath(String.format(assetNameInTable, assetName))).isEmpty())
            {
                if (driver.findElements(nextButtonDisabled).isEmpty())
                {
                    driver.findElement(nextButtonId).click();
                }
                else
                {
                    break;
                }
            }
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

    @Step("Get data from table")
    public String[][] getDataFromTable(String assetName, String[][] dataTable) {
        for (int i = 0; i<7; i++)
        {
            if (!driver.findElement(By.xpath(String.format(dataLocatorInTable, assetName, i+2))).isDisplayed())
                dataTable[i][1] = driver.findElement(By.xpath(String.format(dorpdownDataLocatorInTable, assetName, i+2))).getText();
            else
                dataTable[i][1] = driver.findElement(By.xpath(String.format(dataLocatorInTable, assetName, i+2))).getText();
        }
        return dataTable;
    }

    @Step("Print data from table")
    public void printTableData(String[][] dataTable) {
        for (int i = 0; i<7; i++)
        {
            System.out.println(dataTable[i][0] + " " + dataTable[i][1]);
        }
    }

    @Step("Click Read More")
    public void isReadMoreDisplayed(String assetName) {
        try {
            if (driver.findElements(By.xpath(String.format(tableAssetReadMoreButton, assetName, 9))).isEmpty())
                Thread.sleep(1000);  //required because selenium needs time to find below element -> optimization required
                driver.findElement(By.xpath(String.format(tableAssetReadMoreButton, assetName, 1))).click();
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Click Read More")
    public void clickReadMore(String assetName) {
        try {
            if (driver.findElement(By.xpath(String.format(tableAssetReadMoreButton, assetName, 9))).isDisplayed())
                driver.findElement(By.xpath(String.format(tableAssetReadMoreButton, assetName, 9))).click();
            else
            {
                driver.findElement(By.xpath(String.format(assetReadMoreButtonDropdown, assetName))).click();
            }
        }catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }
}
