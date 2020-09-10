package ru.tetraquark.arcsecondkmp.database

import com.squareup.sqldelight.db.SqlDriver
import ru.tetraquark.arcsecondkmp.model.CoordinateSystem
import ru.tetraquark.arcsecondkmp.model.Coordinates
import ru.tetraquark.arcsecondkmp.model.NumberParameter
import ru.tetraquark.arcsecondkmp.database.Exoplanet as DatabaseExoplanet
import ru.tetraquark.arcsecondkmp.model.Exoplanet as ModelExoplanet

class Database(driver: SqlDriver, private val needCreateScheme: Boolean = true) {

    private val database = ArcsecondDatabase(driver)

    private val exoplanetQueries: ExoplanetQueries
        get() = database.exoplanetQueries

    init {
        if (needCreateScheme) {
            ArcsecondDatabase.Schema.create(driver)
        }
    }

    fun getExoplanet(name: String): ModelExoplanet? {
        return exoplanetQueries.selectByName(name).executeAsOneOrNull()?.mapToModel()
    }

    fun insertExoplanet(exoplanet: ModelExoplanet) {
        if (exoplanetQueries.selectByName(exoplanet.name).executeAsOneOrNull() != null) return

        exoplanetQueries.insertExoplanet(
            name = exoplanet.name,
            parent_star = exoplanet.parentStar,
            detection_method = exoplanet.detectionMethod,
            mass_detection_method = exoplanet.massDetectionMethod,
            radius_detection_method = exoplanet.radiusDetectionMethod,
            coordinates_system_id = exoplanet.coordinates.coordinateSystem?.id?.toLong(),
            coordinates_declination = exoplanet.coordinates.declination?.value,
            coordinates_declination_unit = exoplanet.coordinates.declination?.unit,
            coordinates_epoch = exoplanet.coordinates.epoch,
            coordinates_right_ascension = exoplanet.coordinates.rightAscension?.value,
            coordinates_right_ascension_units = exoplanet.coordinates.rightAscension?.unit,
            mass_value = exoplanet.mass?.value,
            mass_unit = exoplanet.mass?.unit,
            inclination_value = exoplanet.inclination?.value,
            inclination_unit = exoplanet.inclination?.unit,
            eccentricity_value = exoplanet.eccentricity,
            orbital_period_unit = exoplanet.orbitalPeriod?.unit,
            orbital_period_value = exoplanet.orbitalPeriod?.value,
            calculated_temperature_unit = exoplanet.calculatedTemperature?.unit,
            calculated_temperature_value = exoplanet.calculatedTemperature?.value,
            surface_gravity_value = exoplanet.surfaceGravity?.value,
            surface_gravity_unit = exoplanet.surfaceGravity?.unit
        )
    }

    fun insertExoplanets(list: List<ModelExoplanet>) {
        exoplanetQueries.transaction {
            list.forEach(::insertExoplanet)
        }
    }

    fun DatabaseExoplanet.mapToModel() = ModelExoplanet(
        name = name,
        parentStar = parent_star,
        detectionMethod = detection_method,
        massDetectionMethod = mass_detection_method,
        radiusDetectionMethod = radius_detection_method,
        coordinates = coordinates_system_id?.let {
            Coordinates(
                coordinateSystem = CoordinateSystem.getById(it.toInt()),
                rightAscension = NumberParameter(coordinates_right_ascension, coordinates_right_ascension_units),
                declination = NumberParameter(coordinates_declination, coordinates_declination_unit),
                epoch = coordinates_epoch
            )
        } ?: Coordinates(),
        mass = NumberParameter(mass_value, mass_unit),
        radius = NumberParameter(radius_value, radius_unit),
        inclination = NumberParameter(inclination_value, inclination_unit),
        eccentricity = eccentricity_value,
        orbitalPeriod = NumberParameter(orbital_period_value, orbital_period_unit),
        calculatedTemperature = NumberParameter(calculated_temperature_value, calculated_temperature_unit),
        surfaceGravity = NumberParameter(surface_gravity_value, surface_gravity_unit)
    )
}
