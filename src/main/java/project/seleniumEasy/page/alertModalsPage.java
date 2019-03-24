package project.seleniumEasy.page;

import function.functional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// Don't forget add static libraries
import static org.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.*;

import java.util.function.Predicate;

public class alertModalsPage extends functional {
    @FindBy(id = "timingAlert") private WebElement timingAlertBtn;

    public alertModalsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static alertModalsPage using(WebDriver driver) {
        return new alertModalsPage(driver);
    }

    public alertModalsPage ignoreException() {
        driver.get("http://www.seleniumframework.com/Practiceform/");
        Predicate<WebDriver> isAlertPresent = (d) -> {
            d.switchTo().alert();
            return true;
        };
        timingAlertBtn.click();
        await().atMost(30, SECONDS)
                .ignoreExceptions()
                .until(() -> isAlertPresent.test(driver));
        driver.switchTo().alert().accept();
        return this;
    }

    public alertModalsPage atLeast(int least, int most) {
        driver.get("http://www.seleniumframework.com/Practiceform/");
        Predicate<WebDriver> isAlertPresent = (d) -> {
            d.switchTo().alert();
            return true;
        };
        timingAlertBtn.click();
        await().atLeast(least, SECONDS)
                .and()
                .atMost(most, SECONDS)
                .ignoreExceptions()
                .until(() -> isAlertPresent.test(driver));
        driver.switchTo().alert().accept();
        return this;
    }

    public alertModalsPage periodic() {
        driver.get("http://www.seleniumframework.com/Practiceform/");
        Predicate<WebDriver> isAlertPresent = (d) -> {
            d.switchTo().alert();
            return true;
        };
        timingAlertBtn.click();
        /*
        .atMost(2, MINUTES): maximum is 2 minutes
        .pollDelay(1, SECONDS): wait 1 second for the first period time
        .pollInterval(1, SECONDS): check every 10 seconds
        * */
        await().atMost(2, MINUTES)
                .pollDelay(1, SECONDS)
                .pollInterval(1, SECONDS)
                .ignoreExceptions()
                .until(() -> isAlertPresent.test(driver));
        driver.switchTo().alert().accept();
        return this;
    }
}
