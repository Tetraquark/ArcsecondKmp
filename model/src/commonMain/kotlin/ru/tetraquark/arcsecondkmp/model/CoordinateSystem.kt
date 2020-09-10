package ru.tetraquark.arcsecondkmp.model

enum class CoordinateSystem(val id: Int, val title: String) {
    ICRS(0, "ICRS"),
    FK5(1, "FK5"),
    FK4(2, "FK4"),
    FK4_NO_ETERMS(3, "FK4NoETerms"),
    GALACTIC(4, "Galactic"),
    ALT_AZ(5, "AltAz"),
    ICRF(6, "ICRF");

    companion object {
        fun getCoordinatesByTitle(title: String?): CoordinateSystem = when (title) {
            "ICRS" -> ICRS
            "FK5" -> FK5
            "FK4" -> FK4
            "FK4NoETerms" -> FK4_NO_ETERMS
            "Galactic" -> GALACTIC
            "AltAz" -> ALT_AZ
            "ICRF" -> ICRF
            else -> throw IllegalArgumentException("There is no such CoordinateSystem with title $title")
        }

        fun getById(id: Int): CoordinateSystem = when (id) {
            0 -> ICRS
            1 -> FK5
            2 -> FK4
            3 -> FK4_NO_ETERMS
            4 -> GALACTIC
            5 -> ALT_AZ
            6 -> ICRF
            else -> throw IllegalArgumentException("There is no such CoordinateSystem with id $id")
        }
    }
}
