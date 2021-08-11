package yhkim.crawler.domain

import yhkim.crawler.config.Log
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindAll
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import org.springframework.util.StringUtils
import yhkim.crawler.model.CategoryLinkWithName

class Category(driver: WebDriver) : Log {
    @FindAll(
        FindBy(css = "#content > ul > li > a")
    )
    private lateinit var categories: MutableList<WebElement>


    init {
        driver.navigate().to(
            "http://thegillsolution.com/intranet/product/list"
        )
        PageFactory.initElements(DefaultElementLocatorFactory(driver), this)
    }

    fun getCategoriesUrl(): List<CategoryLinkWithName> {
//        return listOf(CategoryLinkWithName("http://thegillsolution.com/intranet/product/list?center=gift1978", "기프트 1센터"))

        return categories
            .map {
                CategoryLinkWithName(
                    url = it.getAttribute("href"),
                    name = StringUtils.trimAllWhitespace(it.getAttribute("text"))
                )
            }
    }
}
