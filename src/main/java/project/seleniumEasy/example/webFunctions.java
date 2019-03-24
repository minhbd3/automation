package project.seleniumEasy.example;

import master.web;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import project.seleniumEasy.page.inputFormsPage;

public class webFunctions extends web {
    final static String TEXT = "Hello";

    @Test
    void functions() {
        inputFormsPage page = new inputFormsPage(driver);
        inputFormsPage
                .using(driver)
                .navigate("https://www.seleniumeasy.com/test/basic-first-form-demo.html")
                .sendValue(page.getMessageBox(), TEXT)
                .click(page.getShowMessageBtn(), "")
                .awaitMessageDisplayed(TEXT)
                .scrollDown(page.getTotalBtn(), "")
                .sendValue(page.getAValue(), RandomStringUtils.randomNumeric(1))
                .sendValue(page.getBValue(), RandomStringUtils.randomNumeric(1))
                .click(page.getTotalBtn(), "")
                .awaitTotalValueDisplayed(String.valueOf((Integer.parseInt(page.getAValue().getAttribute("value"))
                        + Integer.parseInt(page.getBValue().getAttribute("value")))));
    }
}
