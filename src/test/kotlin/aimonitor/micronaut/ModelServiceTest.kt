package aimonitor.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import java.time.Instant
import io.kotest.matchers.shouldBe

@MicronautTest(startApplication = false)
class ModelServiceTest {
    @Inject
    lateinit var modelService: ModelService

    @Test
    fun `process log test`() {
        val inferenceLog = InferenceLog(
            modelId = "huffington-post",
            conversationId = "test-conversation-id",
            startTime = Instant.parse("2023-10-01T12:00:00Z"),
            endTime = Instant.parse("2023-10-01T12:05:00Z"),
            index = 1
        )
        val latency = modelService.processLog(inferenceLog)
        latency shouldBe 300000 // 5 minutes in milliseconds
    }

    @Test
    fun `logs dao test`() {
        val inferenceLog = InferenceLog(
            modelId = "washington-post",
            conversationId = "test-conversation-id",
            startTime = Instant.parse("2023-10-01T12:00:00Z"),
            endTime = Instant.parse("2023-10-01T12:05:00Z"),
            index = 1
        )
        modelService.dao.writeLog(inferenceLog)
        val retrievedLog = modelService.dao.readLog("washington-post", "test-conversation-id")
        retrievedLog shouldBe inferenceLog
    }

    @Test
    fun `metrics dao test`() {
        val metric1 = Metric(
            modelId = "washington-post",
            latency = 300000,
            timeStamp = Instant.parse("2023-10-01T12:05:00Z")
        )
        val metric2 = Metric(
            modelId = "washington-post",
            latency = 200000,
            timeStamp = Instant.parse("2023-10-01T12:10:00Z")
        )
        modelService.dao.writeMetric(metric1)
        val retrievedMetric1 = modelService.dao.getMetricsByModelId("washington-post")
        retrievedMetric1 shouldBe listOf(metric1)
        modelService.dao.writeMetric(metric2)
        val retrievedMetric = modelService.dao.getMetricsByModelId("washington-post")
        retrievedMetric shouldBe listOf(metric1, metric2)
    }
}