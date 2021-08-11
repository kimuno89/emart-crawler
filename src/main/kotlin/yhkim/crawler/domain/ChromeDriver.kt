package yhkim.crawler.domain

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import yhkim.crawler.config.Log
import yhkim.crawler.utils.ExceptionTraceToString
import java.util.concurrent.TimeUnit

class ChromeDriver : Log {
    var driver: WebDriver
    var webDriverWait: WebDriverWait

    init {
        val options = ChromeOptions()
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko")
        options.addArguments("enable-automation");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");


        try {
            driver = ChromeDriver(options)
            driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
            webDriverWait = WebDriverWait(driver, 20L)
        } catch (e: Exception) {
            logger.error(ExceptionTraceToString.toString(e))
            driver = ChromeDriver(options)
            driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
            webDriverWait = WebDriverWait(driver, 20L)
        }
    }
}
