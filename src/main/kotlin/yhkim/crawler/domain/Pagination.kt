package yhkim.crawler.domain

import yhkim.crawler.config.Log
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindAll
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory

class Pagination(driver: WebDriver, private val categoryUrl: String) : Log {
    @FindAll(
        FindBy(css = "#FilteredProducts > div > div.panel.panel-footer > div > div > div > a > span:nth-child(2)"),
    )
    private lateinit var items: MutableList<WebElement>

    init {
        driver.navigate().to(categoryUrl)
        PageFactory.initElements(DefaultElementLocatorFactory(driver), this)
    }

    fun getPaginationUrl(): List<String> {
        if (items.isEmpty()) return listOf()

        val lastPageNumber = items[items.size - 1].text.toInt()
        return (1..lastPageNumber).map { "${categoryUrl}?p=${it}" }
    }
}
