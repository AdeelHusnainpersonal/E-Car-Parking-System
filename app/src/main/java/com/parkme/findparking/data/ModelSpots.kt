package com.parkme.findparking.data

data class ModelSpots(
    val spotName: String,
    val booked: Boolean
) {
    constructor() : this("", false)
}