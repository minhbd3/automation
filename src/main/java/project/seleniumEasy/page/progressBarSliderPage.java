package project.seleniumEasy.page;

import function.functional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class progressBarSliderPage extends functional {
    public progressBarSliderPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static progressBarSliderPage using(WebDriver driver) {
        return new progressBarSliderPage(driver);
    }
}
