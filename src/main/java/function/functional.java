package function;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import enums.notice;
import interfaces.*;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import okhttp3.*;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.tesseract;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.riversun.okhttp3.OkHttp3CookieHelper;
import org.testng.Assert;

import java.io.*;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

public class functional implements extentReport, httpRequest, scanText, spreadSheetAPI, jiraRestAPI, web, copy, appium {
    // define WebDriver & WebDriverWait objects
    protected WebDriver driver;
    protected WebDriverWait wait;

    // define http request objects
    static OkHttpClient client = new OkHttpClient();
    OkHttp3CookieHelper cookieHelper;
    static okhttp3.Request request;
    static Response response;

    // define appium objects
    AppiumDriverLocalService service;
    protected AndroidDriver<MobileElement> android;

    // define google spread sheet api objects

    public static final String APPLICATION_NAME = "Google Sheets API Java";
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final List<String> SCOPES = Collections.singletonList(SheetsScopes.DRIVE);
    // download credentials.json at https://console.developers.google.com/apis/credentials
    public static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    public NetHttpTransport HTTP_TRANSPORT;

    {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // define extent report objects
    protected ExtentHtmlReporter reporter;
    protected static ExtentReports extent;
    protected static ExtentTest log;

    // define regex objects
    protected Pattern pattern;
    protected Matcher matcher;

    // define path to resource
    protected final static String RESOURCE_PATH = System.getProperty("user.dir") + "/src/main/resources/";

    // init headless options for webFunctions browser
    protected List<String> headlessOptions = Arrays.asList("--disable-notifications", "start-maximized", "enable-automation", "--headless",
            "--no-sandbox", "--disable-infobars", "--disable-dev-shm-usage", "--disable-browser-side-navigation", "--disable-gpu");

    /** OVERRIDE EXTENT REPORT INTERFACE **/

    @Override
    public void startExtentReport(String name, String host, String env, String user) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String temp = dateFormat.format(date);
        String cutSpecialChars = temp.replaceAll("\\D+", "").concat("_");
        reporter = new ExtentHtmlReporter(RESOURCE_PATH
                .concat("report/")
                .concat(cutSpecialChars)
                .concat(RandomStringUtils.randomAlphabetic(20))
                .concat(".html"));
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Host Name", host);
        extent.setSystemInfo("Environment", env);
        extent.setSystemInfo("User Name", user);
        reporter.config().setReportName(name);
        reporter.config().setTestViewChartLocation(ChartLocation.TOP);
        reporter.config().setTheme(Theme.STANDARD);
    }

    @Override
    public void flushExtentReport() {
        extent.flush();
    }

    /** OVERRIDE HTTP REQUEST INTERFACE **/

    @Override
    public int requestToGetStatusCode(String url) {
        return request(url);
    }

    @Override
    public String requestToGetBody(String url) {
        String body = null;
        request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        try {
            response = client.newCall(request).execute();
            body = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return body;
    }

    @Override
    public void requestToServer(String type, String url) {
        switch (type) {
            case "desktop":
                request = new okhttp3.Request.Builder().url(url).get().build();
                try {
                    response = client.newCall(request).execute();
                } catch (Exception e) {
                    Assert.fail(e.getMessage());
                }
                break;
            case "mobile":
                cookieHelper = new OkHttp3CookieHelper();
                cookieHelper.setCookie(url, "device_env", "1");
                cookieHelper.setCookie(url, "cdevice", "1");
                cookieHelper.setCookie(url, "device_env_real", "1");
                client = new OkHttpClient.Builder()
                        .cookieJar(cookieHelper.cookieJar())
                        .retryOnConnectionFailure(true)
                        .build();
                request = new okhttp3.Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Cache-Control", "no-cache")
                        .build();
                try {
                    response = client.newCall(request).execute();
                } catch (Exception e) {
                    Assert.fail(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void requestToServerWithAuthentication(String url, String token) {
        request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "102f9b69-883e-4407-a210-bf7d7bd57dfd")
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }


    @Override
    public void requestAndGroupUrlBaseStatusCode(String url, int code) {
        driver.get(url);
        List<Integer> list = Arrays.asList(201, 200, 202, 203, 204, 205, 206, 207, 208, 226);
        if (!list.contains(code))
            assert false;
        /*
        * .stream() : find all elements which has href attribute and process one by one
        * .map(element -> element.getAttribute("href")) : get href value
        * .map(String::trim) : trim the text
        * .distinct() : could be duplicate links, so find unique
        * .collect(Collectors.groupingBy(link -> functional.request(url))); : group links base response code
        * */
        Map<Integer, List<String>> map = driver.findElements(By.xpath("//*[@href]"))
                .stream()
                .map(element -> element.getAttribute("href"))
                .map(String::trim)
                .distinct()
                .collect(Collectors.groupingBy(link -> functional.request(url)));
        System.out.println("Url with status code: " + code);
        map.get(code).stream().forEach(System.out::println);
    }

    private static int request(String url) {
        request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return response.code();
    }

    /** OVERRIDE OCR INTERFACE **/

    void capture(String path) {
        File scrFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
        try {
            String filePath = path + "/ocrImage.png";
            FileUtils.copyFile(scrFile, new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readMessageByOcr(String language) {
        String filePath = RESOURCE_PATH.concat("scanText");
        File file = new File(filePath);
        file.mkdir();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        capture(filePath);
        String ocrText;
        BytePointer outText;
        tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();
        if (api.Init(RESOURCE_PATH.concat("/tessdata"), language) != 0) {
            System.err.println("could not initialize");
            System.exit(1);
        }
        lept.PIX image = pixRead(filePath.concat("\\ocrImage.png"));
        api.SetImage(image);

        // Get OCR result
        outText = api.GetUTF8Text();
        ocrText = outText.getString();
        Assert.assertTrue(!ocrText.isEmpty());

        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);
        return ocrText;
    }

    @Override
    public String readMessageTerminal(String command) {
        String value = "";
        String getValues = "get text values from command: " + command;
        log.log(Status.INFO, getValues);
        try {
            Scanner scanner = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A");
            value = scanner.hasNext() ? scanner.next() : "";
            log.log(Status.PASS, "get text values success");
        } catch (IOException e) {
            log.log(Status.ERROR, "can't " + getValues);
            Assert.fail("can't " + getValues);
        }
        return value;
    }

    /** OVERRIDE SPREAD SHEET API INTERFACE **/

    protected static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        InputStream in = functional.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }


    /**
     * @param sheetId: https://docs.google.com/spreadsheets/d/{sheetId}/edit#gid=0
     * @param range: Sheet1!A2:D5, Sheet1: Name of sheet, A2:D5 data from A2 to D5
     */
    @Override
    public void getRowValueFromSpreadSheet(String sheetId, String range) {
        Sheets service;
        try {
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            ValueRange response = service.spreadsheets().values()
                    .get(sheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty()) {
                System.out.println("no data found.");
            } else {
                for (List row : values) {
                    // Print columns
                    System.out.println(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getExactlyValueFromSpreadSheet(String sheetId, String range) {
        Sheets service;
        try {
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            ValueRange result = service.spreadsheets().values().get(sheetId, range).execute();
            String numRows = result.getValues() != null ? result.getValues().toString() : "";
            System.out.println(numRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeValueToSpreadSheet(ArrayList arrayList, String sheetId, String range) {
        Sheets service;
        try {
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            ValueRange body = new ValueRange()
                    .setValues(Arrays.asList(arrayList));
            UpdateValuesResponse result =
                    service.spreadsheets().values().update(sheetId, range, body)
                            .setValueInputOption("RAW")
                            .execute();
            System.out.printf("%d cells updated.", result.getUpdatedCells());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateValueToSpreadSheet(String spreadsheetId, String title, String find, String replace) {
        Sheets service;
        try {
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            List<com.google.api.services.sheets.v4.model.Request> requests = new ArrayList<>();
            requests.add(new com.google.api.services.sheets.v4.model.Request()
                    .setUpdateSpreadsheetProperties(new UpdateSpreadsheetPropertiesRequest()
                            .setProperties(new SpreadsheetProperties()
                                    .setTitle(title))
                            .setFields("title")));

            // Find and replace text.
            requests.add(new com.google.api.services.sheets.v4.model.Request()
                    .setFindReplace(new FindReplaceRequest()
                            .setFind(find)
                            .setMatchCase(true)
                            .setReplacement("" + replace)
                            .setAllSheets(true)));

            // Add additional requests (operations) ...
            BatchUpdateSpreadsheetRequest body =
                    new BatchUpdateSpreadsheetRequest().setRequests(requests);
            BatchUpdateSpreadsheetResponse response =
                    service.spreadsheets().batchUpdate(spreadsheetId, body).execute();
            FindReplaceResponse findReplaceResponse = response.getReplies().get(1).getFindReplace();
            System.out.printf("%d replacements made.", findReplaceResponse.getOccurrencesChanged());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** OVERRIDE JIRA REST API INTERFACE **/

    protected String getRequestWithAuthentication(String url, String jiraToken) {
        String body = null;
        request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", jiraToken)
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "102f9b69-883e-4407-a210-bf7d7bd57dfd")
                .build();
        try {
            response = client.newCall(request).execute();
            body = response.body().string();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return body;
    }

    @Override
    public void createIssue(String url, String jiraToken, String key, String summary, String description, String issueType,
                            int priority, String label, String originalEstimate, String remainingEstimate, String duedate,
                            String assignee) {
        /* @key: verify key's exist or not, if exist verify next condition, unless throw exception */
        if (!getRequestWithAuthentication(url, jiraToken)
                .contains("\"key\":\"" + key + "\""))
            Assert.fail("invalid key: " + key);

        /* @summary: verify duplicate issue, will search all issues base on key */
        if (getRequestWithAuthentication(url + "/issues/?jql=project%3D" + key + "&maxResults=1000", jiraToken)
                .contains(summary))
            Assert.fail("duplicate issue: " + summary);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                "{\"fields\": {\"project\": {" +
                        "\"key\": \"" + key + "\"}," +
                        "\"summary\": \"" + summary + "\", " +
                        "\"duedate\": \"" + duedate + "\", " +
                        "\"assignee\": {" +
                        "\"name\":\"" + assignee + "\"}," +
                        "\"timetracking\": {" +
                        "\"originalEstimate\": \"" + originalEstimate + "\"," +
                        "\"remainingEstimate\": \"" + remainingEstimate + "\"}," +
                        "\"labels\": [\"" + label + "\"], " +
                        "\"priority\": {\"id\": \"" + priority + "\"}, " +
                        "\"description\": \"" + description + "\", " +
                        "\"issuetype\": {\"name\": \"" + issueType + "\"}}}");

        request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", jiraToken)
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "102f9b69-883e-4407-a210-bf7d7bd57dfd")
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    /** OVERRIDE WEB INTERFACE **/

    @Override
    public void navigate(WebDriver driver, String site) {
        String invalidDomain, cantRequest, wrongStatusCode;
        pattern = Pattern.compile("(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
        matcher = pattern.matcher(site);
        if (!matcher.find()) {
            invalidDomain = notice.INVALID_DOMAIN.getValue() + site;
            log.log(Status.ERROR, invalidDomain);
            Assert.fail(invalidDomain);
        }
        // verify status code and request
        try {
            int code = request(site);
            int index = 0;
            List<Integer> successCode = Arrays.asList(201, 200, 202, 203, 204, 205, 206, 207, 208, 226);
            for (Integer i : successCode) {
                if (code == i) {
                    index++;
                    log.log(Status.PASS, "status code: " + code);
                    log.log(Status.INFO, "navigate to: " + site);
                    driver.get(site);
                    log.log(Status.PASS, "navigate success");
                    break;
                }
            }
            if (index == 0) {
                wrongStatusCode = notice.ERROR_STATUS_CODE.getValue() + code;
                log.log(Status.ERROR, wrongStatusCode);
                Assert.fail(wrongStatusCode);
            }
        } catch (Exception e) {
            cantRequest = notice.CANT_REQUEST.getValue() + site;
            log.log(Status.ERROR, cantRequest);
            Assert.fail(cantRequest);
        }
    }

    @Override
    public void navigateIgnoreAuthentication(WebDriver driver, String site, String username, String password) {
        site = site.replaceAll("://", username + ":" + password + "@");
        navigate(driver, site);
    }

    @Override
    public void click(WebDriver driver, WebElement element, String name) {
        final int DEFAULT_WAIT = 30;
        long start, finish;
        String elementNotDisplayed = name + notice.ELEMENT_NOT_DISPLAYED.getValue() + DEFAULT_WAIT + " seconds";
        String elementCantClick = notice.ELEMENT_CANT_CLICK.getValue() + name;
        try {
            start = System.currentTimeMillis();
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT, TimeUnit.SECONDS);
            element.isDisplayed();
            finish = System.currentTimeMillis();
            log.log(Status.INFO, "element " + name + " displayed on " + (finish - start) + " milliseconds, ready to click action");
            try {
                element.click();
                log.log(Status.PASS, "clicked " + name + " success");
            } catch (Exception e) {
                log.log(Status.ERROR, elementCantClick);
                Assert.fail(elementCantClick);
            }
        } catch (Exception e) {
            log.log(Status.ERROR, elementNotDisplayed);
            Assert.fail(elementNotDisplayed);
        }
    }

    @Override
    public void clickJS(WebDriver driver, WebElement element, String name) {
        JavascriptExecutor script = (JavascriptExecutor) driver;
        String cantClick = notice.ELEMENT_CANT_CLICK.getValue() + name;
        try {
            log.log(Status.INFO, "click: " + name);
            script.executeScript("arguments[0].click();", element);
            log.log(Status.PASS, "click success");
        } catch (Exception e) {
            log.log(Status.ERROR, cantClick);
            Assert.fail(cantClick);
        }
    }

    @Override
    public void sendKey(WebElement element, String content) {
        String cantSendKey = notice.CANT_SEND_VALUE.getValue() + content;
        try {
            log.log(Status.INFO, "send key: " + content);
            element.sendKeys(content);
            log.log(Status.PASS, "sent key success");
        } catch (Exception e) {
            log.log(Status.ERROR, cantSendKey);
            Assert.fail(cantSendKey);
        }
    }

    @Override
    public void sendKeyJS(WebDriver driver, WebElement element, String content) {
        JavascriptExecutor script = (JavascriptExecutor) driver;
        String cantSendKey = notice.CANT_SEND_VALUE.getValue() + content;
        try {
            log.log(Status.INFO, "send key: " + content);
            script.executeScript("arguments[0].value='" + content + "';", element);
            log.log(Status.PASS, "sent key success");
        } catch (Exception e) {
            log.log(Status.ERROR, cantSendKey);
            Assert.fail(cantSendKey);
        }
    }

    @Override
    public void scrollToViewWebElementIgnoreAJAX(WebDriver driver, WebElement element, String name) {
        JavascriptExecutor script = (JavascriptExecutor) driver;
        String cantScrollTo = notice.ELEMENT_CANT_SCROLL_TO.getValue();
        String notDisplayed = name + notice.ELEMENT_NOT_DISPLAYED + " after check";
        // verify element is displayed before scroll
        try {
            log.log(Status.INFO, "verify element displayed before scroll");
            element.isDisplayed();
            log.log(Status.PASS, "element's displayed");
        } catch (Exception e) {
            log.log(Status.ERROR, notDisplayed);
            Assert.fail(notDisplayed);
        }
        // scroll to element
        log.log(Status.INFO, "scroll to view element");
        try {
            script.executeScript("arguments[0].scrollIntoView(true);", element);
            // in case of cant see element, scroll up to view
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
            } catch (Exception e) {
                script.executeScript("window.scrollBy(0,-100)");
            }
        } catch (Exception e) {
            log.log(Status.ERROR, cantScrollTo);
            Assert.fail(cantScrollTo);
        }
        log.log(Status.PASS, "scroll to view success");
    }

    @Override
    public void dragAndDropWebElement(WebDriver driver, WebElement source, WebElement destination) {
        Actions actions = new Actions(driver);
        String cantDragAndDrop = notice.CANT_DRAG_AND_DROP_ELEMENT.getValue();
        try {
            actions.dragAndDrop(source, destination).build().perform();
            log.log(Status.PASS, "drag and drop success");
        } catch (Exception e) {
            log.log(Status.ERROR, cantDragAndDrop);
            Assert.fail(cantDragAndDrop);
        }
    }

    @Override
    public void dragAndDropWebElement(WebDriver driver, WebElement source, int offsetX, int offsetY) {
        Actions actions = new Actions(driver);
        String cantDragAndDrop = notice.CANT_DRAG_AND_DROP_ELEMENT.getValue();
        try {
            actions.dragAndDropBy(source, offsetX, offsetY).build().perform();
            log.log(Status.PASS, "drag and drop success");
        } catch (Exception e) {
            log.log(Status.ERROR, cantDragAndDrop);
            Assert.fail(cantDragAndDrop);
        }
    }

    @Override
    public void switchToFrame(WebDriver driver, WebElement frame) {
        String cantSwitchFrame = notice.CANT_SWITCH_FRAME.getValue();
        log.log(Status.INFO, "switch frame success");
        try {
            driver.switchTo().frame(frame);
            log.log(Status.PASS, "switch frame success");
        } catch(Exception e) {
            log.log(Status.ERROR, cantSwitchFrame);
            Assert.fail(cantSwitchFrame);
        }
    }

    /** OVERRIDE COPY INTERFACE **/

    @Override
    public void copyFile(String sourceFileName, String destinationFileName) {
        try {
            File sourceFile = new File(sourceFileName);
            File destinationFile = new File(destinationFileName);
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void copyFiles(String fileLocationSource, String fileLocationDestination, int numberOfFilesToCopy) {
        File inputLocation = new File(fileLocationSource);
        if (inputLocation.isDirectory()) {
            File[] attachmentFiles = inputLocation.listFiles();
            for (File aFile : attachmentFiles) {
                if (!aFile.isDirectory()) {
                    String fileName = aFile.getName();
                    String sourceFileName = aFile.getAbsolutePath();
                    String destinationFileName = fileLocationDestination
                            + fileName;
                    copyFile(sourceFileName, destinationFileName);
                }
                if (numberOfFilesToCopy >= 0) {
                    if (--numberOfFilesToCopy == 0) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void callAppiumServer(int port) {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "notice")
                .usingPort(port);
        service = builder.build();
        service.start();
    }

    @Override
    public void scrollToView(WebElement element) {
        boolean is = true;
        Dimension size = android.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);
        while (is) {
            try {
                android.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                element.isDisplayed();
                is = false;
            } catch (Exception ignore) {
                is = true;
                android.swipe(startX, startY, startX, endY, 1000);
            }
        }
    }
}
