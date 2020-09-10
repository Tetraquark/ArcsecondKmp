package ru.tetraquark.arcsecondkmp.shared

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import ru.tetraquark.arcsecondkmp.database.ArcsecondDatabase

fun createNativeSqliteDriver(): SqlDriver = NativeSqliteDriver(ArcsecondDatabase.Schema, "database.db")
