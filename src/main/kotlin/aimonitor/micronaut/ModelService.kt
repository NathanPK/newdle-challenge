package aimonitor.micronaut

import jakarta.inject.Inject
import jakarta.inject.Singleton
import mu.KotlinLogging

@Singleton
class ModelService {
    @Inject
    lateinit var dao: Dao

    fun getAllModels(): List<AIModel> {
        // This method should interact with the database to fetch all AI models
        // For now, returning an empty list as a placeholder
        return emptyList()
    }

    fun getModelById(modelId: String): AIModel? {
        // This method should interact with the database to fetch a model by its ID
        // For now, returning null as a placeholder
        val metrics = dao.getMetricsByModelId(modelId)
        val latency =  if (metrics.isNotEmpty()) {
            metrics.map { it.latency }.average().toLong()
        } else {
            0L // Return 0 if no metrics are found
        }
        val count = metrics.size
        logger.info { "Fetched model $modelId with latency $latency and count $count" }
        return AIModel(
            id = modelId,
            avgLatency = latency,
            totalLogs = count
        )
    }

    fun getModelLatency(modelId: String): Long {
        val metrics = dao.getMetricsByModelId(modelId)
        // Calculate the average latency for the given model ID
        val latency =  if (metrics.isNotEmpty()) {
            metrics.map { it.latency }.average().toLong()
        } else {
            0L // Return 0 if no metrics are found
        }
        logger.info { "Average latency for model $modelId: ${metrics.map { it.latency }.average()}" }
        return latency
    }

    fun processLog(log: InferenceLog): Long {
        // This method should process the log and save it to the database
        // For now, it is a placeholder and does not implement any functionality
        logger.info { "Processing log: $log" }
        // Store the log in the database
        dao.writeLog(log)
        // Store the metrics in the database
        val latency = log.endTime.toEpochMilli() - log.startTime.toEpochMilli()
        dao.writeMetric(
            Metric(
                modelId = log.modelId,
                latency = latency,
                timeStamp = log.startTime
            )
        )
        return latency
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}