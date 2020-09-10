package ru.tetraquark.arcsecondkmp.server

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.*
import ru.tetraquark.arcsecondkmp.model.CoordinateSystem
import ru.tetraquark.arcsecondkmp.model.Coordinates
import ru.tetraquark.arcsecondkmp.model.Exoplanet
import ru.tetraquark.arcsecondkmp.model.NumberParameter

class ArcsecondApi(private val httpClient: HttpClient, private val json: Json) {

    suspend fun getExoplanets(page: Int, pageSize: Int): List<Exoplanet> {
        return httpClient.get<String>("$BASE_ENDPOINT/exoplanets") {
            parameter("page", page)
            parameter("page_size", pageSize)
        }.let { result ->
            json.parseToJsonElement(result)
                .jsonObject["results"]
                ?.jsonArray
                ?.map { mapJsonToExoplanet(it.jsonObject) }
                ?: emptyList()
        }
    }

    suspend fun getExoplanetByName(name: String): Exoplanet {
        return httpClient.get<String>("$BASE_ENDPOINT/exoplanets/$name").let { result ->
            json.parseToJsonElement(result).jsonObject.let(::mapJsonToExoplanet)
        }
    }

    private fun mapJsonToExoplanet(element: JsonObject) = Exoplanet(
        name = element["name"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Wrong Exoplanet name."),
        parentStar = element["parent_star"]?.jsonPrimitive?.content.orEmpty(),
        detectionMethod = element["detection_method"]?.jsonPrimitive?.content.orEmpty(),
        massDetectionMethod = element["mass_detection_method"]?.jsonPrimitive?.content.orEmpty(),
        radiusDetectionMethod = element["radius_detection_method"]?.jsonPrimitive?.content.orEmpty(),
        coordinates = element["coordinates"].jsonObjectOrNull()?.let { jsonObj ->
            Coordinates(
                coordinateSystem = CoordinateSystem.getCoordinatesByTitle(jsonObj["system"]?.jsonPrimitive?.content),
                rightAscension = NumberParameter(
                    jsonObj["right_ascension"]?.jsonPrimitive?.doubleOrNull,
                    jsonObj["right_ascension_units"]?.jsonPrimitive?.content.orEmpty()
                ),
                declination = NumberParameter(
                    jsonObj["declination"]?.jsonPrimitive?.doubleOrNull,
                    jsonObj["declination_units"]?.jsonPrimitive?.content.orEmpty()
                ),
                epoch = jsonObj["epoch"]?.jsonPrimitive?.double ?: .0
            )
        } ?: Coordinates(),
        mass = element["mass"].jsonObjectOrNull()?.let(::jsonObjectToNumberParameter),
        radius = element["radius"].jsonObjectOrNull()?.let(::jsonObjectToNumberParameter),
        inclination = element["inclination"].jsonObjectOrNull()?.let(::jsonObjectToNumberParameter),
        eccentricity = element["eccentricity"].jsonObjectOrNull()?.get("value")?.jsonPrimitive?.doubleOrNull,
        orbitalPeriod = element["orbital_period"].jsonObjectOrNull()?.let(::jsonObjectToNumberParameter),
        calculatedTemperature = element["calculated_temperature"].jsonObjectOrNull()?.let(::jsonObjectToNumberParameter),
        surfaceGravity = element["surface_gravity"].jsonObjectOrNull()?.let(::jsonObjectToNumberParameter)
    )

    private fun jsonObjectToNumberParameter(jsonObject: JsonObject): NumberParameter? = NumberParameter(
        jsonObject["value"]?.jsonPrimitive?.doubleOrNull,
        jsonObject["unit"]?.jsonPrimitive?.content.orEmpty()
    )

    private fun JsonElement?.jsonObjectOrNull() = if (this != null && this != JsonNull) jsonObject else null

    companion object {
        const val BASE_ENDPOINT = "https://api.arcsecond.io"
    }

}
