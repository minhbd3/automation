package project.seleniumEasy.example;

import master.web;
import org.testng.annotations.Test;
import project.seleniumEasy.page.alertModalsPage;
import project.seleniumEasy.page.otherPage;

public class await extends web {
    /*  Click on a ‘Download’ button – a File Download progress dialog appears.
    We are waiting for the File download progress bar to show the Complete! message. */
    @Test
    void waitingForElementToAppear() {
        otherPage.using(driver).waitAppear();
    }

    /* Click on the ‘Get New User’ button, an AJAX request is made to get a new user information.
    A loading message is shown as shown below till the request is complete.
    So if we need to automate this flow, need to wait for the loading message to disappear.*/
    @Test
    void waitingForElementToDisappear() {
        otherPage.using(driver).waitDisappear();
    }

    /* When we try to switch to alert when it is not present, it throws an exception
    We handle that using ‘ignoreExceptions()’*/
    @Test
    void ignoreExceptionWhenSwitchAlert() {
        alertModalsPage.using(driver).ignoreException();
    }

    /* Alert should not appear within S1 seconds - max S2 seconds - ignore alert not present exception */
    @Test
    void atLeast() {
        alertModalsPage.using(driver).atLeast(2, 5);
    }

    /* Check for something at regular intervals until the given timeout period. */
    @Test
    void periodicCheck() {
        alertModalsPage.using(driver).periodic();
    }
}
