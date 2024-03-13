package com.parkme.findparking.data

data class ModelMyCar(
    val carName: String,
    val carNumber: String,
    var docId: String? = null
){
    constructor():this("","")
}
