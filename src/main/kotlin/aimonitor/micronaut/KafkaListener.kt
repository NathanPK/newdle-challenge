package aimonitor.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import jakarta.inject.Inject

@KafkaListener
class LogListener @Inject constructor(
    private val modelService: ModelService
) {
    @Topic("logs-ingest")
    fun receive(log: InferenceLog) {
        modelService.processLog(log)
    }
}