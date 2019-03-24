package master;

import com.aventstack.extentreports.Status;
import function.functional;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class web extends functional {
    Properties properties;
    BufferedReader reader;

    @BeforeTest
    protected void config() {
        /* load properties to webFunctions before main */
        try {
            reader = new BufferedReader(new FileReader(RESOURCE_PATH + "properties/web.properties"));
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

        /*
          begin extent report which a HTML report, write what you want to a HTML file  need to care only first parameter
          @path which path to save the report, it configs on startExtentReport() function, default will save to resources
          other parameters such as: @name, @host, @env, @user which you can fill whatever you want
         */
        startExtentReport("name", "localhost", "MacOS", "minhbd3@fpt.com.vn");
        log = extent.createTest("config");

        /* load browsers property to decide open which browsers */
        switch (properties.getProperty("browser")) {
            case "chrome":
                /* check headless mode config for chrome */
                log.log(Status.INFO, "{browser : chrome}");
                ChromeOptions options = new ChromeOptions();
                // path to Chrome Binary, recent using MacOS
                options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
                if (!properties.getProperty("headless").equals("true")) {
                    log.log(Status.INFO, "{headless : false}");
                    WebDriverManager.chromedriver().version("71").setup();

                    driver = new ChromeDriver(options);
                } else {
                    headlessOptions.forEach(options::addArguments);
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(options);
                    log.log(Status.INFO, "{headless : true}");
                }
                break;
            case "firefox":
                /* check headless mode config for firefox */
                log.log(Status.INFO, "{browser : firefox}");
                if (!properties.getProperty("headless").equals("true")) {
                    log.log(Status.INFO, "{headless : false}");
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                } else {
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    headlessOptions.forEach(firefoxOptions::addArguments);
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(firefoxOptions);
                    log.log(Status.INFO, "{headless : true}");
                }
                break;
            case "opera":
                /*
                if you're using windows, you've to set binary to use Opera browser
                OperaOptions options = new OperaOptions();
                options.setBinary("//.opera.exe");
                */

                log.log(Status.INFO, "{browser : opera}");
                /* check headless mode config for opera */
                if (!properties.getProperty("headless").equals("true")) {
                    log.log(Status.INFO, "{headless : false}");
                    WebDriverManager.operadriver().setup();
                    driver = new OperaDriver();
                } else {
                    Assert.fail("{headless : not support}");
                }
                break;
            case "safari":
                /*
                WebDriverManager'has not supported on Safari, must to download Safari driver and set property to it.
                Must download valid format on Windows
                */

                /* check headless mode config for safari */
                log.log(Status.INFO, "{browser : safari}");
                if (!properties.getProperty("headless").equals("true")) {
                    log.log(Status.INFO, "{headless : false}");
                    System.getProperty("webdriver.safari.driver", RESOURCE_PATH);
                    driver = new SafariDriver();
                    break;
                } else {
                    Assert.fail("{headless : not support}");
                }
                /* Don't config on Edge & Internet Explorer on MacOS because it's not supported on MacOS, but you can use on Windows */
            case "edge":
                /* check headless mode config for edge */
                if (!properties.getProperty("headless").equals("true")) {
                    log.log(Status.INFO, "{headless : false}");
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    log.log(Status.INFO, "{browser : edge}");
                } else {
                    Assert.fail("{headless : not support}");
                }
                break;
            case "internet explorer":
                /* check headless mode config for internet explorer */
                log.log(Status.INFO, "{browser : internet explorer}");
                if (!properties.getProperty("headless").equals("true")) {
                    log.log(Status.INFO, "{headless : false}");
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                } else {
                    Assert.fail("{headless : not support}");
                }
                break;
            case "webDriver/phantomjs":
                /*
                Must download phantomjs.exe can download from http://phantomjs.org/download.html download base on OS
                Selenium discontinue to support Phantomjs and they recommended use headless mode on Chrome or Firefox
                System.setProperty("phantomjs.binary.path", RESOURCE_PATH + "phantomjs");
                WebDriverManager.phantomjs().setup();
                driver = new PhantomJSDriver();
                log.log(Status.INFO, "{browser : phantomjs}");
                */

                Assert.fail("{headless : selenium discontinue supported}");
                break;
            default:
                break;
        }

        /*
          load maximize property to decide open full windows or not, no need if you set on headless browser mode
          list headless browsers {chrome, firefox, phantomjs, htmlUnit, Splash, TrifleJS, SimpleBrowser}

          if phantomjs:
          log.log(Status.INFO, "{headless : true}");
          log.log(Status.INFO, "{maximize : true}");
         */
        if (properties.getProperty("headless").equals("true")) {
            /* maximize auto set to true on options */
            log.log(Status.INFO, "{maximize : true}");
        } else {
            if (properties.getProperty("maximize").equals("true")) {
                log.log(Status.INFO, "{maximize : true}");
                driver.manage().window().maximize();
            } else {
                log.log(Status.INFO, "{maximize : false}");
            }
        }

        wait = new WebDriverWait(driver, 30);
    }

    @AfterMethod
    protected void saveResultReport(ITestResult result) {
        /*
          flush extent report, report will not create if you do not call this function, it'll change report after a method
          can config to generate report or not
         */
        if (properties.getProperty("report").equals("true"))
            flushExtentReport();
    }

    @AfterTest
    protected void tearDown() {
        /*
          destroy webFunctions driver after main
          can change generate report to @AfterTest method, but its not overwrite
         */
        driver.quit();
    }
}
