package ru.tetraquark.arcsecondkmp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.tetraquark.arcsecondkmp.shared.planetdetails.PlanetDetailsFragment
import ru.tetraquark.arcsecondkmp.shared.planetdetails.PlanetDetailsRouter
import ru.tetraquark.arcsecondkmp.shared.planetslist.PlanetListFragment
import ru.tetraquark.arcsecondkmp.shared.planetslist.PlanetsListRouter

private const val TAG_PLANET_DETAILS_FRAGMENT = "details_screen"

class MainActivity : AppCompatActivity(), PlanetDetailsRouter, PlanetsListRouter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showPlanetsListFragment()
    }

    fun showPlanetsListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.root_view, PlanetListFragment())
            .commit()
    }

    override fun navigateToPlanetDetails(planetName: String) {
        val args = Bundle().apply {
            putParcelable(
                PlanetDetailsFragment.DetailsArgs.ARGS_KEY,
                PlanetDetailsFragment.DetailsArgs(planetName)
            )
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.root_view, PlanetDetailsFragment::class.java, args, TAG_PLANET_DETAILS_FRAGMENT)
            .addToBackStack(TAG_PLANET_DETAILS_FRAGMENT)
            .commit()
    }

    override fun navigateBack() {
        supportFragmentManager.popBackStack()
    }
}
