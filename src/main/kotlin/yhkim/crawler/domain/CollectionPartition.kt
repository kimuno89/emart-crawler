package yhkim.crawler.domain

import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors


class CollectionPartition {
    companion object {
        fun <T> partition(list: List<T>, size: Int): Collection<List<T>> {
            val counter = AtomicInteger(0)
            return list.stream()
                .collect(Collectors.groupingBy { counter.getAndIncrement() / size })
                .values
        }
    }
}