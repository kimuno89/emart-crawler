package yhkim.crawler.domain

import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.springframework.util.StringUtils

internal class UploadTest {
    @Test
    fun uploadTest() {
        val chromeDriver = ChromeDriver()
        val driver = chromeDriver.driver

        DisknLogin(driver).login("lotem4", "wnsla4fkd!")
        Thread.sleep(2000)

        driver.findElements(By.xpath("/html/body/div[3]/ul/li[1]/ul/li"))
            .find { StringUtils.trimAllWhitespace(it.text) == "thegill" }
            ?.findElement(By.cssSelector("a"))
            ?.click()

        Thread.sleep(1000)

        driver.findElements(By.xpath("/html/body/div[3]/ul/li[1]/ul/li/ul/li"))
            .find { StringUtils.trimAllWhitespace(it.text) == "gift1978" }
            ?.findElement(By.cssSelector("a"))
            ?.click()


        Thread.sleep(1000)

        driver.findElement(By.cssSelector("#btn_upload")).click()
        driver.findElement(By.xpath("/html/body/div[10]/div[1]/div[4]/div/input"))
            .sendKeys("/Users/yoonho/workspace/kmong-crawler/thegill-crawler/images/main-Y91.jpg \n /Users/yoonho/workspace/kmong-crawler/thegill-crawler/images/main-Y49.jpg \n /Users/yoonho/workspace/kmong-crawler/thegill-crawler/images/main-X2M.jpg");
        println(1)
        driver.quit()
    }
}
