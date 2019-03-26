package project.seleniumEasy.example;

import master.web;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import project.seleniumEasy.page.inputFormsPage;

public class webFunctions extends web {
    inputFormsPage page;

    @BeforeTest
    void init() {
        page = new inputFormsPage(driver);
    }

    /* Clicking on the checkbox will display a success message */
    @Test
    void functions() {
        final String TEXT = "Hello";
        inputFormsPage
                .using(driver)
                .navigate("https://www.seleniumeasy.com/test/basic-first-form-demo.html")
                .sendValue(page.getMessageBox(), TEXT)
                .click(page.getShowMessageBtn(), "")
                .awaitMessage(page.getMessageDisplayed(), TEXT)

                .scrollDown(page.getTotalBtn(), "")
                .sendValue(page.getAValue(), RandomStringUtils.randomNumeric(1))
                .sendValue(page.getBValue(), RandomStringUtils.randomNumeric(1))
                .click(page.getTotalBtn(), "")
                .awaitMessage(page.getTotalValue(), String.valueOf((Integer.parseInt(page.getAValue().getAttribute("value"))
                        + Integer.parseInt(page.getBValue().getAttribute("value")))));
    }

    /* Check the below points before automating
            lick on 'Check All' to check all checkboxes at once.
            When you check all the checkboxes, button will change to 'Uncheck All'
            When you uncheck at least one checkbox, button will change to 'Check All' */
    @Test
    void functionWithCheckbox() {
        inputFormsPage
                .using(driver)
                .navigate("https://www.seleniumeasy.com/test/basic-checkbox-demo.html")
                .click(page.getSingleCheckbox(), "")
                .awaitMessage(page.getSingleCheckboxMessage(), "Success - Check box is checked")

                .scrollDown(page.getCheckAllBtn(), "")
                .click(page.getCheckAllBtn(), "")
                .checkClickableAllCheckbox()
                .click(page.getCheckAllBtn(), "")
                .checkNotClickableAllCheckbox();
    }

    @Test
    void functionWithRadioButton() {
        final String RADIO_BUTTON_IS_NOT_CHECKED = "Radio button is Not checked";
        final String RADIO_BUTTON_MALE_IS_CHECKED = "Radio button 'Male' is checked";

        inputFormsPage
                .using(driver)
                .navigate("https://www.seleniumeasy.com/test/basic-radiobutton-demo.html")
                .click(page.getCheckedValueButton(), "")
                .awaitMessage(page.getSexValueText(), RADIO_BUTTON_IS_NOT_CHECKED)
                .click(page.getMaleButton(), "")
                .awaitMessage(page.getSexValueText(), RADIO_BUTTON_IS_NOT_CHECKED)
                .click(page.getCheckedValueButton(), "")
                .awaitMessage(page.getSexValueText(), RADIO_BUTTON_MALE_IS_CHECKED)
                .click(page.getFemaleButton(), "")
                .awaitMessage(page.getSexValueText(), RADIO_BUTTON_MALE_IS_CHECKED)
                .click(page.getCheckedValueButton(), "")
                .awaitMessage(page.getSexValueText(), "Radio button 'Female' is checked")

                .scrollDown(page.getValueGroupRadioButton(), "")
                .click(page.getValueGroupRadioButton(), "")
                .awaitMessage(page.getTotalValueGroupRadio(), "Sex :\nAge group:")
                .clickRandomSexBelowAndProgress();
    }

    @Test
    void functionWithDropdown() {
        inputFormsPage
                .using(driver)
                .navigate("https://www.seleniumeasy.com/test/basic-select-dropdown-demo.html");
        String temp = page.getDaySelectBoxDisplayedList()
                .get((int)(Math.random() * page.getDaySelectBoxDisplayedList().size() + 0)).getText();
        page.sendValue(page.getDaySelectBox(), temp);
        page.awaitMessage(page.getDaySelectBoxDisplayed(), "Day selected :- " + temp);
    }

    @Test
    void functionWithValidation() {
        inputFormsPage
                .using(driver)
                .navigate("https://www.seleniumeasy.com/test/input-form-demo.html")
                .randomInputAllFields();
    }

    /* Submitting the form via an AJAX call will not refresh the page.
    Loading icon will be shown as progress about the server side execution.
    On processing, the submit button will be hidden to prevent duplicate submits.
    */
    @Test
    void ajaxFormSubmit() {
        inputFormsPage
                .using(driver)
                .navigate("https://www.seleniumeasy.com/test/ajax-form-submit-demo.html")
                .submitFormByAJAX();
    }
}
