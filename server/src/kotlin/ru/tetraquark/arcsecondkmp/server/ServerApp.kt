package ru.tetraquark.arcsecondkmp.server

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.features.DefaultHeaders
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import ru.tetraquark.arcsecondkmp.model.CheckResult
import ru.tetraquark.arcsecondkmp.model.Exoplanet
import ru.tetraquark.arcsecondkmp.model.ExoplanetPage
import ru.tetraquark.arcsecondkmp.database.Database

private val dbDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
private val database = Database(dbDriver)

private val httpClient: HttpClient by lazy {
    HttpClient(CIO)
}

private val json: Json by lazy {
    Json {
        ignoreUnknownKeys = true
    }
}

private val arcsecondApi: ArcsecondApi by lazy {
    ArcsecondApi(httpClient, json)
}

private val logger = LoggerFactory.getLogger("Application")

fun Application.main() {
    initApp()
    routings()
}

private fun Application.initApp() {
    install(DefaultHeaders)
}

private fun Application.routings() {
    routing {

        get("/exoplanet/list") {
            val page = call.request.queryParameters["page"]?.toInt()
            val offset = call.request.queryParameters["offset"]?.toInt()

            if (page == null || offset == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val pageResult = ExoplanetPage(arcsecondApi.getExoplanets(page + 1, offset), page, offset)
                call.respondText(
                    text = pageResult.asJson(ExoplanetPage.serializer()),
                    contentType = ContentType.Application.Json
                )
            } catch (cause: Exception) {
                logger.error("/exoplanet/list?page=$page&offset=$offset", cause)
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/exoplanet") {
            val name = call.request.queryParameters["name"]

            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val planet = database.getExoplanet(name) ?: arcsecondApi.getExoplanetByName(name).also {
                    database.insertExoplanets(listOf(it))
                }

                call.respondText(
                    text = planet.asJson(Exoplanet.serializer()),
                    contentType = ContentType.Application.Json
                )
            } catch (cause: Exception) {
                logger.error("/exoplanet?name=$name", cause)
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/exoplanet/check") {
            val hash = call.request.queryParameters["hash"]?.toInt()
            val name = call.request.queryParameters["name"]

            if (hash == null || name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val planet = database.getExoplanet(name)
                if (planet == null || planet.hashCode() != hash) {
                    call.respondText(
                        text = CheckResult(false).asJson(CheckResult.serializer()),
                        contentType = ContentType.Application.Json
                    )
                } else {
                    call.respondText(
                        text = CheckResult(true).asJson(CheckResult.serializer()),
                        contentType = ContentType.Application.Json
                    )
                }
            } catch (cause: Exception) {
                logger.error("/exoplanet/check?hash=$hash&name=$name", cause)
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

private fun <T> T.asJson(serializer: SerializationStrategy<T>): String {
    return json.encodeToJsonElement(serializer, this).toString()
}
