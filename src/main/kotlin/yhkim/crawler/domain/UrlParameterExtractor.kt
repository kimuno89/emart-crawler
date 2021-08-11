package yhkim.crawler.domain

import yhkim.crawler.config.Log
import java.net.URL

class UrlParameterExtractor(private val urlString: String?) : Log {

    private val queryMap = mutableMapOf<String, String>()

    init {
        urlString?.let {
            try {
                val queries = URL(it).query.split("&")
                queries.forEach { query ->
                    val keyValue = query.split("=")
                    queryMap[keyValue[0]] = keyValue[1]
                }
            } catch (e: Exception) {
                e.printStackTrace()
                logger.error(it)
            }

        }

    }

    fun getQueryParameter(key: String): String? {
        return queryMap[key]
    }

    fun movePage(key: String, value: Int): String {
        queryMap[key] = value.toString()
        val url = URL(urlString)
        val query = queryMap.map { entry -> "${entry.key}=${entry.value}" }.toList().joinToString("&")
        return "${url.protocol}://${url.host}${url.path}?${query}"
    }
}
