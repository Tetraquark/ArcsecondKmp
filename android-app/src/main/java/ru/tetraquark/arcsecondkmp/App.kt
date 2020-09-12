package ru.tetraquark.arcsecondkmp

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.tetraquark.arcsecondkmp.database.ArcsecondDatabase
import ru.tetraquark.arcsecondkmp.shared.SharedApp
import ru.tetraquark.arcsecondkmp.shared.planetdetails.DepsProvider as PlanetDetailsDepsProvider
import ru.tetraquark.arcsecondkmp.shared.planetslist.DepsProvider as PlanetsListDepsProvider
import ru.tetraquark.arcsecondkmp.ui.ArcsecondKmpAppTheme

class App : Application() {

    private lateinit var shardApp: SharedApp

    override fun onCreate() {
        super.onCreate()

        val sqlDriver = createSqlDriver(this)

        shardApp = SharedApp(sqlDriver)
        initApp()
    }

    fun initApp() {
        val appTheme: @Composable (@Composable () -> Unit) -> Unit = {
            ArcsecondKmpAppTheme(content = it)
        }

        PlanetsListDepsProvider.planetsListModuleFactory = shardApp.planetsListFactory
        PlanetsListDepsProvider.appTheme = appTheme

        PlanetDetailsDepsProvider.planetDetailsModuleFactory = shardApp.planetDetailsFactory
        PlanetDetailsDepsProvider.appTheme = appTheme
    }

    fun createSqlDriver(appContext: Context): SqlDriver
            = AndroidSqliteDriver(ArcsecondDatabase.Schema, appContext, "database.db")
}
