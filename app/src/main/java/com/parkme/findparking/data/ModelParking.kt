package com.parkme.findparking.data

data class ModelParking(
    var spaceImage: String,
    val spaceName: String,
    val spaceLocationInText: String,
    val pricePerHour: String,
    val numberOfSpots: Int,
    val spaceMapLocation: String,
    var docId: String = ""
) {
    constructor() : this("", "", "", "", 0, "")
}
