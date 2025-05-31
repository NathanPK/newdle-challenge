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
    fun getModel(@PathVariable id: Long): HttpResponse<AIModel> {
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
}