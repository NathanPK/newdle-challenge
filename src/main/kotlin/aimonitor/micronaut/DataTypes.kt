package aimonitor.micronaut

import java.time.Instant
import java.time.LocalDateTime

data class AIModel(
    val id: Long,
    val name: String,
    val description: String,
    val version: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class InferenceLog(
    val modelId: String,
    val conversationId: String,
    val startTime: Instant,
    val endTime: Instant
)