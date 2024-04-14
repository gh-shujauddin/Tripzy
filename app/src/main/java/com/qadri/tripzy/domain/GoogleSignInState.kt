package com.qadri.tripzy.domain

import com.google.firebase.firestore.GeoPoint

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val location: GeoPoint
)

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)

data class ConfirmEmail(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)

data class SaveUserDetail(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
