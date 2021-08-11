package yhkim.crawler.component

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ActiveProfile(
    @Value("\${spring.profiles.active}")
    val activeProfile: String
) {
    fun isProduction(): Boolean {
        return activeProfile == "pro"
    }

    fun isTest(): Boolean {
        return activeProfile == "test"
    }
}
