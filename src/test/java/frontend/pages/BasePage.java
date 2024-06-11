package frontend.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    public static final String URL = "https://www.xm.com/";

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to Home page")
    public void navigateTo() {
        driver.get(URL);
    }

    @Step("Get page title")
    public String getTitle()
    {
        return driver.getTitle();
    }
}
