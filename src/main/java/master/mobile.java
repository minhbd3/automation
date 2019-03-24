package master;

import function.functional;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class mobile extends functional {
    Properties properties;
    BufferedReader reader;

    @BeforeTest
    protected void config() {
        /* load properties to webFunctions before main */
        try {
            reader = new BufferedReader(new FileReader(RESOURCE_PATH + "properties/android.properties"));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("configuration.properties not found");
        }

        /* set capabilities */
        DesiredCapabilities caps = new DesiredCapabilities();
        Enumeration<String> enums = (Enumeration<String>) properties.propertyNames();
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        String key, value;
        while (enums.hasMoreElements()) {
            key = enums.nextElement();
            value = properties.getProperty(key);
            keys.add(key);
            values.add(value);
        }
        for (int i = 0; i < keys.size(); i++) {
            caps.setCapability(keys.get(i), values.get(i));
        }

        /* start appium server */
        try {
            callAppiumServer(4788);
        } catch (Exception e) {
            Assert.fail("can't connect appium");
        }

        try {
            android = new AndroidDriver<>(new URL("http://0.0.0.0:4788/wd/hub"), caps);
            wait = new WebDriverWait(android, 30);
        } catch (Exception e) {
            Assert.fail("can't start driver");
        }
    }

    @AfterTest
    protected void destroy() {
        android.quit();
    }
}
