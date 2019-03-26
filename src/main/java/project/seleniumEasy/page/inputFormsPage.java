package project.seleniumEasy.page;

import function.functional;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.person.Person;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

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
    @FindBy(id = "isAgeSelected") private WebElement singleCheckbox;
    @FindBy(id = "txtAge") private WebElement singleCheckboxMessage;
    @FindBy(id = "check1") private WebElement checkAllBtn;
    @FindBy(xpath = "//*[contains(text(),'Check the below points before automating')]//following::div[@class='checkbox']/label/input") private List<WebElement> listMultipleCheckbox;
    @FindBy(xpath = "//input[@value='Male' and @name='optradio']") private WebElement maleButton;
    @FindBy(xpath = "//input[@value='Female' and @name='optradio']") private WebElement femaleButton;
    @FindBy(xpath = "//input[@name='gender']") private List<WebElement> sexBelowButton;
    @FindBy(id = "buttoncheck") private WebElement checkedValueButton;
    @FindBy(xpath = "//*[@class='radiobutton']") private WebElement sexValueText;
    @FindBy(xpath = "//input[@name='ageGroup']") private List<WebElement> ageGroup;
    @FindBy(xpath = "//button[text()='Get values']") private WebElement valueGroupRadioButton;
    @FindBy(xpath = "//*[@class='groupradiobutton']") private WebElement totalValueGroupRadio;
    @FindBy(id = "select-demo") private WebElement daySelectBox;
    @FindBy(xpath = "//*[@id='select-demo']/option") private List<WebElement> daySelectBoxDisplayedList;
    @FindBy(xpath = "//p[@class='selected-value']") private WebElement daySelectBoxDisplayed;
    @FindBy(name = "first_name") private WebElement firstName;
    @FindBy(name = "last_name") private WebElement lastName;
    @FindBy(name = "email") private WebElement emailAddress;
    @FindBy(name = "phone") private WebElement phoneNumber;
    @FindBy(name = "address") private WebElement address;
    @FindBy(name = "city") private WebElement city;
    @FindBy(name = "state") private WebElement state;
    @FindBy(xpath = "//*[@name='state']/option") private List<WebElement> stateList;
    @FindBy(name = "zip") private WebElement zipCode;
    @FindBy(name = "website") private WebElement domain;
    @FindBy(name = "hosting") private List<WebElement> haveHosting;
    @FindBy(name = "comment") private WebElement description;
    @FindBy(xpath = "//button[@type='submit']") private WebElement sendBtn;
    @FindBy(id = "title") private WebElement ajaxName;
    @FindBy(id = "description") private WebElement ajaxComment;
    @FindBy(id = "btn-submit") private WebElement ajaxSubmitBtn;
    @FindBy(id = "submit-control") private WebElement ajaxMessage;

    public WebElement getMessageBox() {return messageBox;}
    public WebElement getMessageDisplayed() {return messageDisplayed;}
    public WebElement getShowMessageBtn() {return showMessageBtn;}
    public WebElement getAValue() {return aValue;}
    public WebElement getBValue() {return bValue;}
    public WebElement getTotalBtn() {return totalBtn;}
    public WebElement getTotalValue() {return totalValue;}
    public WebElement getSingleCheckbox() {return singleCheckbox;}
    public WebElement getSingleCheckboxMessage() {return singleCheckboxMessage;}
    public WebElement getCheckAllBtn() {return checkAllBtn;}
    public WebElement getCheckedValueButton() {return checkedValueButton;}
    public WebElement getSexValueText() {return sexValueText;}
    public WebElement getMaleButton() {return maleButton;}
    public WebElement getFemaleButton() {return femaleButton;}
    public WebElement getValueGroupRadioButton() {return valueGroupRadioButton;}
    public WebElement getTotalValueGroupRadio() {return totalValueGroupRadio;}
    public WebElement getDaySelectBox() {return daySelectBox;}
    public WebElement getDaySelectBoxDisplayed() {return daySelectBoxDisplayed;}
    public List<WebElement> getDaySelectBoxDisplayedList() {return daySelectBoxDisplayedList;}

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

    public inputFormsPage awaitMessage(WebElement element, String text) {
        await().atMost(5, SECONDS).until(element::getText, is(text));
        return this;
    }

    public inputFormsPage scrollDown(WebElement element, String name) {
        scrollToViewWebElementIgnoreAJAX(driver, element, name);
        return this;
    }

    public inputFormsPage checkClickableAllCheckbox() {
        for (WebElement item : listMultipleCheckbox) {
            Assert.assertFalse(!item.isSelected());
        }
        return this;
    }

    public inputFormsPage checkNotClickableAllCheckbox() {
        for (WebElement item : listMultipleCheckbox) {
            Assert.assertFalse(item.isSelected());
        }
        return this;
    }

    public inputFormsPage clickRandomSexBelowAndProgress() {
        int sex = (int)(Math.random() * sexBelowButton.size() + 0);
        int age = (int)(Math.random() * ageGroup.size() + 0);
        sexBelowButton.get(sex).click();
        ageGroup.get(age).click();
        valueGroupRadioButton.click();
        String text = "Sex : " + sexBelowButton.get(sex).getAttribute("value")
                + " Age group: " + ageGroup.get(age).getAttribute("value");
        Assert.assertEquals(totalValueGroupRadio.getAttribute("textContent"), text);
        return this;
    }

    public inputFormsPage randomInputAllFields() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        Company company = fairy.company();
        sendKey(firstName, person.getFirstName());
        sendKey(lastName, person.getLastName());
        sendKey(emailAddress, person.getEmail());
        sendKey(phoneNumber, person.getTelephoneNumber());
        sendKey(address, person.getAddress().getAddressLine1());
        sendKey(city, person.getAddress().getCity());
        sendKey(state, String.valueOf(stateList.get((int)(Math.random() * stateList.size() + 0)).getText()));
        sendKey(zipCode, person.getAddress().getPostalCode());
        sendKey(domain, company.getDomain());
        click(driver, haveHosting.get((int)(Math.random() * haveHosting.size() + 0)), "");
        sendKey(description, RandomStringUtils.randomAlphabetic(20));
        click(driver, sendBtn, "");
        return this;
    }

    public inputFormsPage submitFormByAJAX() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        click(driver, ajaxSubmitBtn, "");
        await().atMost(10, SECONDS).until(ajaxMessage::getText, not("Form submited Successfully!"));
        sendKey(ajaxName, person.getFullName());
        sendKey(ajaxComment, RandomStringUtils.randomAlphabetic(20));
        click(driver, ajaxSubmitBtn, "");
        await().atMost(10, SECONDS).until(ajaxMessage::getText, is("Form submited Successfully!"));
        return this;
    }
}
