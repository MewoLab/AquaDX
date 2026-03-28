package icu.samnyan.aqua.spring

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Timer
import java.util.concurrent.ConcurrentHashMap
import io.micrometer.core.instrument.Metrics as MMetrics

object Metrics {
    fun counter(metricName: String, vararg pairs: Pair<String, Any>): Counter {
        val expandedLabels = expandLabels(*pairs)
        return cache.computeIfAbsent(MetricCacheKey(Counter::class.java, metricName, expandedLabels)) {
            Counter
                .builder(metricName)
                .tags(*expandedLabels)
                .register(MMetrics.globalRegistry)
        } as Counter
    }

    fun timer(metricName: String, vararg pairs: Pair<String, Any>): Timer {
        val expandedLabels = expandLabels(*pairs)
        return cache.computeIfAbsent(MetricCacheKey(Timer::class.java, metricName, expandedLabels)) {
            Timer
                .builder(metricName)
                .publishPercentiles(0.5, 0.75, 0.90, 0.95, 0.99)
                .tags(*expandedLabels)
                .register(MMetrics.globalRegistry)
        } as Timer
    }

    private data class MetricCacheKey(
        val type: Class<*>,
        val metricName: String,
        val expandedLabels: Array<String>,
    )

    private val cache = ConcurrentHashMap<MetricCacheKey, Any>()

    private fun expandLabels(vararg pairs: Pair<String, Any>): Array<String> {
        return pairs
            .flatMap {
                listOf(it.first, it.second.toString())
            }
            .toTypedArray()
    }
}
