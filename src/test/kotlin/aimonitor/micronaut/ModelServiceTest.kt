package aimonitor.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import java.time.Instant
import io.kotest.matchers.shouldBe

@MicronautTest
class ModelServiceTest {
    @Inject
    lateinit var modelService: ModelService

    @Test
    fun `process log test`() {
        val inferenceLog = InferenceLog(
            modelId = "washington-post",
            conversationId = "test-conversation-id",
            startTime = Instant.parse("2023-10-01T12:00:00Z"),
            endTime = Instant.parse("2023-10-01T12:05:00Z"),
        )
        val latency = modelService.processLog(inferenceLog)
        latency shouldBe 300000 // 5 minutes in milliseconds
    }
}