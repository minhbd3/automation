package interfaces;

import org.openqa.selenium.WebElement;

public interface appium {
    void callAppiumServer(int port);
    void scrollToView(WebElement element);
}
