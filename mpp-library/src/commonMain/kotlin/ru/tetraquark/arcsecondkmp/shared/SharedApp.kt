package ru.tetraquark.arcsecondkmp.shared

import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import ru.tetraquark.arcsecondkmp.database.Database
import ru.tetraquark.arcsecondkmp.model.Exoplanet
import ru.tetraquark.arcsecondkmp.shared.data.ArcsecondApi
import ru.tetraquark.arcsecondkmp.shared.data.ExoplanetRepository
import ru.tetraquark.arcsecondkmp.shared.planetdetails.PlanetDetailsModuleFactory
import ru.tetraquark.arcsecondkmp.shared.planetslist.PlanetsListModuleFactory

class SharedApp(
    sqlDriver: SqlDriver,
    private val serverAddress: String = "http://10.0.2.2:8080"
) {

    private val httpClient: HttpClient by lazy {
        HttpClient {
            expectSuccess = false

            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }

        }
    }

    private val arcsecondApi: ArcsecondApi by lazy {
        ArcsecondApi(httpClient, serverAddress)
    }

    private val database: Database by lazy {
        Database(sqlDriver, false)
    }

    private val exoplanetRepository: ExoplanetRepository by lazy {
        ExoplanetRepository(
            api = arcsecondApi,
            database = database
        )
    }

    val planetsListFactory = PlanetsListModuleFactory(
        exoplanetRepository = exoplanetRepository
    )

    val planetDetailsFactory = PlanetDetailsModuleFactory(
        exoplanetRepository = exoplanetRepository
    )
}
