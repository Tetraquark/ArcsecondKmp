object Modules {
    val model = MultiPlatformModule(
        name = ":model",
        exported = true
    )
    val database = MultiPlatformModule(
        name = ":database",
        exported = true
    )
    val planetsList = MultiPlatformModule(
        name = ":mpp-library:feature:planets-list",
        exported = true
    )
    val planetDetails = MultiPlatformModule(
        name = ":mpp-library:feature:planet-details",
        exported = true
    )
}
