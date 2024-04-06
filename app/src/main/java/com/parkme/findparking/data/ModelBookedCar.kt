package com.parkme.findparking.data

data class ModelBookedCar(
    val carName: String,
    val carNumber: String,
    val parkingSpaceName: String,
    val spotName: String,
    val durationHours: Int,
    val spaceLocation: String,
    var docId: String = "",
    var endTimeSeconds: Long = 0
) {
    constructor() : this("", "", "", "", 0, "")
}
