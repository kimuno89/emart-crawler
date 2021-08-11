package yhkim.crawler.domain

import yhkim.crawler.config.Log
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory

class DisknLogin(driver: WebDriver) : Log {
    @FindBy(xpath = "//*[@id=\"input_id\"]")
    private lateinit var username: WebElement

    @FindBy(xpath = "//*[@id=\"input_pw\"]")
    private lateinit var password: WebElement

    @FindBy(xpath = "//*[@id=\"submit\"]")
    private lateinit var loginButton: WebElement

    init {
        driver.navigate().to(
            "https://www.diskn.com/customer/login"
        )
        PageFactory.initElements(DefaultElementLocatorFactory(driver), this)
    }

    fun login(id: String, pwd: String): Boolean = try {
        username.sendKeys(id)
        password.sendKeys(pwd)
        loginButton.click()

        true
    } catch (e: Exception) {
        logger.error("로그인에 실패했습니다. ${e.localizedMessage}")
        e.printStackTrace()
        false
    }
}
