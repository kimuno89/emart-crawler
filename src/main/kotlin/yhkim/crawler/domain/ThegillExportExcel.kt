package yhkim.crawler.domain

import excelkt.Sheet
import excelkt.workbook
import excelkt.write
import org.apache.poi.ss.usermodel.IndexedColors
import yhkim.crawler.config.Log
import yhkim.crawler.model.Product
import java.time.LocalDate

class ThegillExportExcel(private val data: List<Product>, private val category: String) : Log {
    fun exportExcel() {
        logger.info("export Excel.. $category")
        workbook {
            sheet(category) {
                customersHeader()
                row(data)
            }

        }.write("더길-${category}-${LocalDate.now()}.xlsx")
    }

    private fun Sheet.customersHeader() {
        val headings = listOf(
            "상품코드",
            "카테고리",
            "상품명",
            "소비자가",
            "셀러공급가",
            "박스입수량",
            "매입처",
            "면과세구분",
            "무료배송구분",
            "재고유무",
            "합포장가능구분",
            "옵션명",
            "대표이미지",
            "상세이미지",
        )

        val headingStyle = createCellStyle()
        val font = createFont()
        font.fontName = "IMPACT"
        font.color = IndexedColors.BLACK.index

        headingStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        headingStyle.setFont(font)

        row(headingStyle) {
            headings.forEach { cell(it) }
        }
    }

    private fun Sheet.row(data: List<Product>) {
        val rowStyle = createCellStyle()
        data.forEach {
            row(rowStyle) {
                cell(it.productCode)
                cell(it.productCategory)
                cell(it.productName)
                cell(it.user)
                cell(it.seller)
                cell(it.box)
                cell(it.company)
                cell(it.gwase)
                cell(it.shipping)
                cell(it.stock)
                cell(it.plusShipping)
                cell(it.optionName)
                cell(it.productMainImage)
                cell(it.productDetailImage)
            }
        }
    }
}
