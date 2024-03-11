package com.parkme.findparking.data

data class ModelUser(
    val email: String,
    val password: String,
    val fullName: String,
    val dob: String,
    val phoneNumber:String,
    val address: String
){
    constructor():this("","","","","","",)
}
