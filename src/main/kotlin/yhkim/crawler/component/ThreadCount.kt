package yhkim.crawler.component

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ThreadCount(
    @Value("\${thread.count}")
    val threadCount: Int
)

