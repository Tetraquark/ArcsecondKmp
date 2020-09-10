package ru.tetraquark.arcsecondkmp.shared.data

import io.ktor.client.*
import io.ktor.client.request.*
import ru.tetraquark.arcsecondkmp.model.CheckResult
import ru.tetraquark.arcsecondkmp.model.Exoplanet
import ru.tetraquark.arcsecondkmp.model.ExoplanetPage

/**
 * TODO: replace with moko-network
 */
class ArcsecondApi(
    private val httpClient: HttpClient,
    private val serverAddress: String
) {

    suspend fun getExoplanets(page: Int, offset: Int): ExoplanetPage {
        return httpClient.get("$serverAddress/exoplanet/list") {
            parameter("page", page)
            parameter("offset", offset)
        }
    }

    suspend fun getExoplanetByName(name: String): Exoplanet {
        return httpClient.get("$serverAddress/exoplanet") {
            parameter("name", name)
        }
    }

    suspend fun checkExoplanet(name: String, hash: Int): CheckResult {
        return httpClient.get("$serverAddress/exoplanet/check") {
            parameter("name", name)
            parameter("hash", hash)
        }
    }
}
