package frontend.tests;

import frontend.pages.HomePage;
import frontend.pages.AssetPage;
import frontend.pages.StocksPage;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.awt.*;

public class CompareStocksDataTest {
    WebDriver driver;

    @DataProvider(name = "screenSize")
    public static Object[][] screenSize() {
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Object[][] {{(int) screenSize.getWidth(),(int) screenSize.getHeight()}, {1024, 768}, {800, 600}};
    }

    @BeforeMethod
    public void launchBrowser() {
        System.out.println("Launching Chrome browser");
        System.setProperty("webdriver.chrome.driver", "./src/main/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
    }

    //Set up Friefox - for browser selection can be created method
//    @BeforeMethod
//    public void launchBrowser() {
//        System.out.println("Launching Firefox browser");
//        System.setProperty("webdriver.firefox.driver", "./src/main/resources/drivers/geckodriver.exe");
//        driver = new FirefoxDriver();
//    }

    @Test(dataProvider = "screenSize", description = "Compare stocks data on 2 pages (Stocks and Read More pages)")
    public void verifyHomepageTitle(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
        // Home page TC part
        HomePage homePage = new HomePage(driver);
        Assert.assertEquals(homePage.getTitle(), "Forex & CFD Trading on Stocks, Indices, Oil, Gold by XM™");
        homePage.clickMainMenuButton("trading");
        //Assert.assertTrue(homePage.mainMenuPopupDisplayed("Trading"));
        homePage.clickTradingMenuButton("stocks");
        // Stocks page TC part
        StocksPage stocksPage = new StocksPage(driver);
        Assert.assertEquals(stocksPage.getTitle(), "Trading Global Stocks With A Regulated Broker");
        stocksPage.scrollPageToCountryList();
        stocksPage.closeStickyBarIfPresent();
        stocksPage.clickCountryButton("Norway");
        stocksPage.scrollPageToTable();
        String assetName = "Orkla ASA (ORK.OL)"; // if parametrization needed for many assets -> add this to param list and DataProvider
        stocksPage.findInstrumentInTable(assetName);
        stocksPage.isReadMoreDisplayed(assetName);
        String[][] data = { //assuming that the list of parameters in the table will not change
                {"Symbols",""},
                {"Spread as low as (quote currency)",""},
                {"Min/Max Trade Size",""},
                {"Min Margin Percentage",""},
                {"Swap Value in Margin Currency Long",""},
                {"Swap Value in Margin Currency Short",""},
                {"Limit and Stop Levels**",""}};
        data = stocksPage.getDataFromTable(assetName, data);
        stocksPage.printTableData(data);
        stocksPage.clickReadMore(assetName);
        // Asset page TC part
        AssetPage assetPage = new AssetPage(driver);
        Assert.assertEquals(stocksPage.getTitle(), "Trade Orkla ASA (ORK.OL) Stocks at XM | ORK.OL Online Trading"); // String formatting for other assets if needed
        assetPage.scrollPageToTradingConditions();

        String tradingTableValue = "";
        for (int i=0; i<7; i++)
        {
            tradingTableValue = assetPage.getDataFromTable(data[i][0]);
            Assert.assertEquals(tradingTableValue, data[i][1]);
        }
    }

    @AfterMethod
    public void terminateBrowser(){
        driver.close();
    }
}