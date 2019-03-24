package project.seleniumEasy.page;

import function.functional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// Don't forget add static libraries
import static org.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.*;
import static org.hamcrest.Matchers.*;

public class otherPage extends functional {
    WebDriver driver;

    @FindBy(id = "downloadButton") private WebElement startDownloadBtn;
    @FindBy(css = "div.progress-label") private WebElement progressBar;
    @FindBy(id = "save") private WebElement saveBtn;
    @FindBy(id = "save") private WebElement loading;

    public otherPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static otherPage using(WebDriver driver) {
        return new otherPage(driver);
    }

    public otherPage waitAppear() {
        driver.get("http://www.seleniumeasy.com/test/jquery-download-progress-bar-demo.html");
        startDownloadBtn.click();
        await().atMost(30, SECONDS).until(progressBar::getText, is("Complete!"));
        return this;
    }

    public otherPage waitDisappear() {
        driver.get("http://www.seleniumeasy.com/test/dynamic-data-loading-demo.html");
        saveBtn.click();
        await().atMost(30, SECONDS).until(loading::getText, not("loading..."));
        return this;
    }
}
