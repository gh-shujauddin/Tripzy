package com.qadri.tripzy.domain

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
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
