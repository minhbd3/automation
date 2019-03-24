package project.seleniumEasy.example;

import master.web;
import org.testng.annotations.Test;

public class readText extends web {
    @Test
    void ocr() {
        driver.get("https://google.com");
        // don't forget download train data, https://github.com/tesseract-ocr/tessdata
        System.out.println(readMessageByOcr("ENG"));
    }

    @Test
    void readTerminal() {
        // read text from your terminal
        System.out.println(readMessageTerminal("echo Hello!"));
    }
}
