package project.seleniumEasy.example;

import function.functional;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * In first time, it'll navigate to google and you've to login, then set permission to account
 */

public class googleSpreadSheetAPI extends functional {
    // please, change your sheet and range below :)
    final static String SHEET_EXAMPLE = "";

    @Test
    void readValue() {
        getRowValueFromSpreadSheet(SHEET_EXAMPLE, "Sheet1!A2:D5");
    }

    @Test
    void readExactlyValue() {
        getExactlyValueFromSpreadSheet(SHEET_EXAMPLE, "Sheet1!C3");
    }

    @Test
    void writeValue() {
        ArrayList<String> list = new ArrayList<>();
        list.add(RandomStringUtils.randomAlphabetic(10));
        writeValueToSpreadSheet(list, SHEET_EXAMPLE, "Sheet1!A1");
    }

    @Test
    void updateValue() {
        updateValueToSpreadSheet(SHEET_EXAMPLE, "", "", "");
    }
}
