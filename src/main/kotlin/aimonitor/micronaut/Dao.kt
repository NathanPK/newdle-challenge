package aimonitor.micronaut

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import javax.sql.DataSource

@Singleton
@Transactional
open class Dao {
    @Inject
    private lateinit var dataSource: DataSource

    open fun getMetricsByModelId(modelId: String): List<Metric> {
        val sql = "SELECT model_id, latency, time_stamp FROM $METRICS_TABLE_NAME WHERE model_id = ?"
        val metrics = mutableListOf<Metric>()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, modelId)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        metrics.add(
                            Metric(
                                modelId = rs.getString("model_id"),
                                latency = rs.getLong("latency"),
                                timeStamp = rs.getTimestamp("time_stamp").toInstant()
                            )
                        )
                    }
                }
            }
        }
        return metrics
    }

    open fun writeMetric(metric: Metric) {
        val sql = """
            INSERT INTO $METRICS_TABLE_NAME (model_id, latency, time_stamp)
            VALUES (?, ?, ?)
        """.trimIndent()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, metric.modelId)
                stmt.setLong(2, metric.latency)
                stmt.setTimestamp(3, java.sql.Timestamp.from(metric.timeStamp))
                stmt.executeUpdate()
            }
        }
    }

    open fun writeLog(log: InferenceLog) {
        val sql = "INSERT INTO $LOGS_TABLE_NAME (model_id, conversation_id, start_time, end_time, log_index) VALUES (?, ?, ?, ?, ?)"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, log.modelId)
                stmt.setString(2, log.conversationId)
                stmt.setTimestamp(3, java.sql.Timestamp.from(log.startTime))
                stmt.setTimestamp(4, java.sql.Timestamp.from(log.endTime))
                stmt.setInt(5, log.index)
                stmt.executeUpdate()
            }
        }
    }

    open fun readLog(modelId: String, conversationId: String): InferenceLog? {
        val sql = "SELECT model_id, conversation_id, start_time, end_time, log_index FROM $LOGS_TABLE_NAME WHERE model_id = ? AND conversation_id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, modelId)
                stmt.setString(2, conversationId)
                stmt.executeQuery().use { rs ->
                    return if (rs.next()) {
                        InferenceLog(
                            modelId = rs.getString("model_id"),
                            conversationId = rs.getString("conversation_id"),
                            startTime = rs.getTimestamp("start_time").toInstant(),
                            endTime = rs.getTimestamp("end_time").toInstant(),
                            index = rs.getInt("log_index")
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    companion object {
        private val logger = mu.KotlinLogging.logger {}

        private const val METRICS_TABLE_NAME = "metrics"
        private const val LOGS_TABLE_NAME = "logs"
    }
}