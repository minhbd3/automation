package project.seleniumEasy.page;

import function.functional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class datePickersPage extends functional {
    public datePickersPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static datePickersPage using(WebDriver driver) {
        return new datePickersPage(driver);
    }
}
