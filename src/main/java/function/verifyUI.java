package function;

import com.galenframework.testng.GalenTestNgTestBase;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Collections;

/**
 * Open a page in browser
 * Resize it to specified size
 * Test layout according to user-defined specs
 */
public class verifyUI extends GalenTestNgTestBase {
    
    @Override
    public WebDriver createDriver(Object[] objects) {
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver();
    }

    /**
     * @param url
     * @param path
     * @param type
     * @// TODO: 2019-03-20 : Use galen framework to verify layout base on user-defined specified 
     */
    @Parameters({"url", "path", "type"})
    @Test
    void verifyLayout(String url, String path, String type) throws Exception {
        load(url, 1024, 768);
        checkLayout(path, Collections.singletonList(type));
    }
}
