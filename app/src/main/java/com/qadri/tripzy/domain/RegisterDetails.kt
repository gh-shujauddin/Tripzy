package com.qadri.tripzy.domain

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDetails(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)

@Serializable
data class RegisterResponse(
    val code: String = "",
    val message: String = "",
    val email: String = ""
)
