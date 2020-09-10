package ru.tetraquark.arcsecondkmp

import android.app.Application
import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.tetraquark.arcsecondkmp.database.ArcsecondDatabase
import ru.tetraquark.arcsecondkmp.shared.SharedApp
import ru.tetraquark.arcsecondkmp.shared.planetslist.DepsProvider
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
        DepsProvider.planetsListModuleFactory = shardApp.planetsListFactory
        DepsProvider.appTheme = {
            ArcsecondKmpAppTheme(content = it)
        }
    }

    fun createSqlDriver(appContext: Context): SqlDriver
            = AndroidSqliteDriver(ArcsecondDatabase.Schema, appContext, "database.db")
}
