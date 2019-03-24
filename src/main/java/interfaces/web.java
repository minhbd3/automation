package interfaces;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface web {
    void navigate(WebDriver driver, String site);
    void navigateIgnoreAuthentication(WebDriver driver, String site, String username, String password);
    void click(WebDriver driver, WebElement element, String name);
    void clickJS(WebDriver driver, WebElement element, String name);
    void sendKey(WebElement element, String content);
    void sendKeyJS(WebDriver driver, WebElement element, String content);
    void scrollToViewWebElementIgnoreAJAX(WebDriver driver, WebElement element, String name);
    void dragAndDropWebElement(WebDriver driver, WebElement source, WebElement destination);
    void dragAndDropWebElement(WebDriver driver, WebElement source, int offsetX, int offsetY);
    void switchToFrame(WebDriver driver, WebElement frame);
}
