package aimonitor.micronaut

import java.time.Instant
import java.time.LocalDateTime

data class AIModel(
    val id: String,
    val avgLatency: Long,
    val totalLogs: Int,
)

data class InferenceLog(
    val modelId: String,
    val conversationId: String,
    val startTime: Instant,
    val endTime: Instant,
    val index: Int
)

data class Metric(
    val modelId: String,
    val latency: Long,
    val timeStamp: Instant,
)