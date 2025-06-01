package aimonitor.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory

@Controller("/api/v1/metrics")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Validated
class ModelController(
    private val modelService: ModelService
) {
    private val logger = LoggerFactory.getLogger(ModelController::class.java)

    @Get
    fun getAllModels(): HttpResponse<List<AIModel>> {
        return try {
            val models = modelService.getAllModels()
            HttpResponse.ok(models)
        } catch (e: Exception) {
            logger.error("Error fetching models", e)
            HttpResponse.serverError()
        }
    }

    @Get("/{id}")
    fun getModel(@PathVariable id: String): HttpResponse<AIModel> {
        return try {
            val model = modelService.getModelById(id)
            if (model != null) {
                HttpResponse.ok(model)
            } else {
                HttpResponse.notFound()
            }
        } catch (e: Exception) {
            logger.error("Error fetching model $id", e)
            HttpResponse.serverError()
        }
    }

    @Get("/timeline/{id}")
    fun getModelTimeline(@PathVariable id: String): HttpResponse<List<Metric>> {
        return try {
            val timeline = modelService.getModelTimeline(id)
            if (timeline.isNotEmpty()) {
                HttpResponse.ok(timeline)
            } else {
                HttpResponse.notFound()
            }
        } catch (e: Exception) {
            logger.error("Error fetching timeline for model $id", e)
            HttpResponse.serverError()
        }
    }

    @Get("/comparison/{id1}/{id2}")
    fun compareModels(@PathVariable id1: String, @PathVariable id2: String): HttpResponse<Map<String, Any>> {
        return try {
            val model1 = modelService.getModelById(id1)
            val model2 = modelService.getModelById(id2)

            if (model1 == null || model2 == null) {
                return HttpResponse.notFound("One or both models not found") as HttpResponse<Map<String, Any>>
            }

            val comparison = mapOf(
                "model1" to model1,
                "model2" to model2,
                "latencyDifference" to (model1.avgLatency - model2.avgLatency),
                "totalLogsDifference" to (model1.totalLogs - model2.totalLogs)
            )

            HttpResponse.ok(comparison)
        } catch (e: Exception) {
            logger.error("Error comparing models $id1 and $id2", e)
            HttpResponse.serverError()
        }
    }
}