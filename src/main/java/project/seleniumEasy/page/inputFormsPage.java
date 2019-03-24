package project.seleniumEasy.page;

import function.functional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;

public class inputFormsPage extends functional {
    @FindBy(id = "user-message") private WebElement messageBox;
    @FindBy(xpath = "//button[contains(text(),'Show Message')]") private WebElement showMessageBtn;
    @FindBy(id = "display") private WebElement messageDisplayed;
    @FindBy(id = "sum1") private WebElement aValue;
    @FindBy(id = "sum2") private WebElement bValue;
    @FindBy(id = "displayvalue") private WebElement totalValue;
    @FindBy(xpath = "//button[contains(text(),'Get Total')]") private WebElement totalBtn;

    public WebElement getMessageBox() {return messageBox;}
    public WebElement getShowMessageBtn() {return showMessageBtn;}
    public WebElement getAValue() {return aValue;}
    public WebElement getBValue() {return bValue;}
    public WebElement getTotalBtn() {return totalBtn;}

    public inputFormsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static inputFormsPage using(WebDriver driver) {
        return new inputFormsPage(driver);
    }

    public inputFormsPage navigate(String site) {
        navigate(driver, site);
        return this;
    }

    public inputFormsPage click(WebElement element, String name) {
        click(driver, element, name);
        return this;
    }

    public inputFormsPage sendValue(WebElement element, String content) {
        sendKey(element, content);
        return this;
    }

    public inputFormsPage awaitMessageDisplayed(String text) {
        await().atMost(30, SECONDS).until(messageDisplayed::getText, is(text));
        return this;
    }

    public inputFormsPage awaitTotalValueDisplayed(String text) {
        await().atMost(30, SECONDS).until(totalValue::getText, is(text));
        return this;
    }

    public inputFormsPage scrollDown(WebElement element, String name) {
        scrollToViewWebElementIgnoreAJAX(driver, element, name);
        return this;
    }
}
