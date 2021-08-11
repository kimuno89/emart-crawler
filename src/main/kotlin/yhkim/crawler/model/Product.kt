package yhkim.crawler.model

data class Product(
    var productCode: String,
    val productCategory: String,
    val productName: String,
    val user: Int,
    val seller: Int,
    val box: Int,
    val company: String,
    val gwase: String,
    val shipping: String,
    val stock: String,
    val plusShipping: String,
    val optionName: String,
    var productMainImage: String,
    var productDetailImage: String,
) {
    fun changeCodeAndImage(category: String) {
        productCode = "${category}-${productCode}"
        productMainImage = "https://lotem4.diskn.com/thegill/${category}/main-${productCode}.jpg"
        productDetailImage = "<img src=\"https://lotem4.diskn.com/thegill/${category}/detail-${productCode}.jpg\" />"
    }

}
