package project.seleniumEasy.page;

import function.functional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class tablePage extends functional {
    public tablePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static tablePage using(WebDriver driver) {
        return new tablePage(driver);
    }
}
