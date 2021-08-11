package yhkim.crawler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.io.FileUtils
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import yhkim.crawler.config.Log
import yhkim.crawler.domain.*
import yhkim.crawler.model.Product
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URL
import javax.annotation.PostConstruct
import javax.imageio.ImageIO
import kotlin.system.exitProcess


@Component
class CrawlerStarter : Log {
    @PostConstruct
    fun start() {
        CoroutineScope(Dispatchers.Default).launch {
            val chromeDriver = ChromeDriver()
            val driver = chromeDriver.driver
            Login(driver).login("cellsk", "wnsla4fkd!")
            delay(1000)

            val categories = Category(driver).getCategoriesUrl()
            categories.forEachIndexed { categoryIndex, it ->
                deleteBeforeFile()

                logger.info(it.url)

                driver.navigate().to(it.url)
                delay(1000)
                downloadFile(driver)
                delay(1000)

                val files = File("${System.getProperty("user.home")}/Downloads").listFiles()
                val file = files.first { file -> file.name.contains("seller_prduct") }
                val excelProducts = readFromExcelFile(file.inputStream())

                val lastNumber = try {
                    val url = driver.findElement(By.cssSelector("#sb-paging > a.last")).getAttribute("href")
                    val pageNumber = UrlParameterExtractor(url).getQueryParameter("page")
                    pageNumber?.toInt() ?: 1
                } catch (e: Exception) {
                    // ignore any exceptions
                    1
                }

                val categoryCodeName = UrlParameterExtractor(it.url).getQueryParameter("center")!!

                (1..lastNumber).forEach { pageNumber ->
                    driver.navigate().to("${it.url}&page=${pageNumber}")
                    delay(500)

                    val rows = driver.findElements(By.cssSelector("#list_form > table > tbody > tr"))

                    rows.map { row ->
                        val productCode = row.findElement(By.cssSelector("td:nth-child(5)")).text
                        val productMainImage =
                            row.findElement(By.cssSelector("td:nth-child(13) > a")).getAttribute("href")
                        val productDetailImage =
                            row.findElement(By.cssSelector("td:nth-child(14) > a")).getAttribute("href")

                        excelProducts
                            .find { excel -> excel.productCode == productCode }
                            ?.changeCodeAndImage(categoryCodeName)

                        downloadImage(productMainImage, productCode, "main-", categoryCodeName)
                        downloadImage(productDetailImage, productCode, "detail-", categoryCodeName)
                    }
                }


                uploadImageFiles(driver, categoryCodeName, categoryIndex)

                deleteImageFolder()

                ThegillExportExcel(excelProducts, it.name).exportExcel()
            }

            driver.close()
            driver.quit()
            exitProcess(-1)
        }
    }

    private suspend fun uploadImageFiles(driver: WebDriver, categoryCodeName: String?, categoryIndex: Int) {
        if (categoryIndex == 0) {
            DisknLogin(driver).login("lotem4", "wnsla4fkd!")
            delay(3000)
        } else {
            driver.navigate().to("https://www.diskn.com/disk/lotem4")
        }

        driver.findElements(By.xpath("/html/body/div[3]/ul/li[1]/ul/li"))
            .find { StringUtils.trimAllWhitespace(it.text) == "thegill" }
            ?.findElement(By.cssSelector("a"))
            ?.click()

        delay(3000)

        driver.findElements(By.xpath("/html/body/div[3]/ul/li[1]/ul/li[18]/ul/li"))
            .find { StringUtils.trimAllWhitespace(it.text) == categoryCodeName }
            ?.findElement(By.cssSelector("a"))
            ?.click()

        delay(3000)

        val imageFiles = File("images").listFiles()
            .map { file -> file.absolutePath }


        CollectionPartition.partition(imageFiles, 50).forEach {
            driver.findElement(By.cssSelector("#btn_upload")).click()
            driver.findElement(By.xpath("/html/body/div[10]/div[1]/div[4]/div/input"))
                .sendKeys(it.joinToString(" \n "))

            delay(3000)

            driver.findElement(By.cssSelector("#uploader_start")).click()
            val uploadWindow = driver.findElement(By.cssSelector("#uploaderContainer"))
            while (uploadWindow.isDisplayed) {
                delay(1000)
                logger.info("Upload Files...")
            }

            delay(2000)
        }
    }

    private fun deleteBeforeFile() {
        File("${System.getProperty("user.home")}/Downloads").listFiles()
            .filter { file -> file.name.contains("seller_prduct") }.map { file -> file.delete() }
    }

    private fun downloadFile(driver: WebDriver) {
        driver.findElement(By.cssSelector("#content > div.tbl-tot > div.btnWrap2 > div > a")).click()
    }

    private fun readFromExcelFile(inputStream: FileInputStream): List<Product> {
        val products = mutableListOf<Product>()

        WorkbookFactory.create(inputStream).use { xlWb ->
            val xlWs = xlWb.getSheetAt(0)

            xlWs.rowIterator().forEach {
                if (it.rowNum == 0) {
                    return@forEach
                }

                val product = Product(
                    productCode = it.getCell(0).toString(),
                    productCategory = it.getCell(1).toString(),
                    productName = it.getCell(2).toString(),
                    user = it.getCell(3).numericCellValue.toInt(),
                    seller = it.getCell(4).numericCellValue.toInt(),
                    box = it.getCell(5).numericCellValue.toInt(),
                    company = it.getCell(6).toString(),
                    gwase = it.getCell(7).toString(),
                    shipping = it.getCell(8).toString(),
                    stock = it.getCell(9).toString(),
                    plusShipping = it.getCell(10).toString(),
                    optionName = it.getCell(11).toString(),
                    productMainImage = "",
                    productDetailImage = "",
                )
                products.add(product)
            }

        }

        return products
    }

    private fun downloadImage(image: String, productCode: String, prefix: String, category: String) = try {
        makeFolder()
        val url = URL(image)
        val extension: String = image.substring(image.lastIndexOf('.') + 1)

        val image: BufferedImage = ImageIO.read(url)
        val file = File("./images/${prefix}${category}-${productCode}.jpg")

        ImageIO.write(image, extension, file)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    private fun makeFolder() {
        val path = "images"
        val folder = File(path)
        if (!folder.exists()) {
            try {
                folder.mkdir()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteImageFolder() {
        FileUtils.deleteDirectory(File("images"));
    }
}
