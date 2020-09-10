package ru.tetraquark.arcsecondkmp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.tetraquark.arcsecondkmp.shared.planetslist.PlanetListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.root_view, PlanetListFragment())
            .commit()
    }
}
