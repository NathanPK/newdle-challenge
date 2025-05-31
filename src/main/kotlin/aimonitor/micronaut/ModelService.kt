package aimonitor.micronaut

import jakarta.inject.Singleton
import mu.KotlinLogging

@Singleton
class ModelService {
    fun getAllModels(): List<AIModel> {
        // This method should interact with the database to fetch all AI models
        // For now, returning an empty list as a placeholder
        return emptyList()
    }

    fun getModelById(id: Long): AIModel? {
        // This method should interact with the database to fetch a specific AI model by ID
        // For now, returning null as a placeholder
        return null
    }

    fun processLog(log: InferenceLog): Long {
        // This method should process the log and save it to the database
        // For now, it is a placeholder and does not implement any functionality
        logger.info { "Processing log: $log" }
        // return the latency of the inference in milliseconds
        val latency = log.endTime.toEpochMilli() - log.startTime.toEpochMilli()
        return latency
    }

    companion object {
        // kotlin logger
        private val logger = KotlinLogging.logger {}
    }
}