package com.qadri.tripzy.presentation.auth

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.qadri.tripzy.data.TripzyRepository
import com.qadri.tripzy.domain.ApiResult
import com.qadri.tripzy.domain.ConfirmEmail
import com.qadri.tripzy.domain.LocationResult
import com.qadri.tripzy.domain.RegisterDetails
import com.qadri.tripzy.domain.RegisterResponse
import com.qadri.tripzy.domain.RegisterState
import com.qadri.tripzy.domain.Resource
import com.qadri.tripzy.domain.SaveUserDetail
import com.qadri.tripzy.domain.SignInState1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: TripzyRepository
) : ViewModel() {

    private val _signUpState = Channel<RegisterState>()
    val signUpState = _signUpState.receiveAsFlow()

    private val _confirmEmailState = Channel<ConfirmEmail>()
    val confirmEmail = _confirmEmailState.receiveAsFlow()

    private val _saveUserState = Channel<SaveUserDetail>()
    val saveUserState = _saveUserState.receiveAsFlow()

    private val _isEmailVerified = MutableStateFlow(false)
    val isEmailVerified = _isEmailVerified.asStateFlow()


    private val _updateDetailStatus = Channel<LoginStatus>()
    val updateDetailsStatus = _updateDetailStatus.receiveAsFlow()

    fun sendConfirmationEmail() = viewModelScope.launch {
        repository.confirmUser().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _confirmEmailState.send(ConfirmEmail(isError = result.message))
                }

                is Resource.Loading -> {
                    _confirmEmailState.send(ConfirmEmail(isLoading = true))
                }

                is Resource.Success -> {
                    _confirmEmailState.send(ConfirmEmail(isSuccess = "Email sent Successfully"))
                }
            }

        }
    }

    fun reloadEmailVerified() {
        println(FirebaseAuth.getInstance().currentUser?.isEmailVerified)
        FirebaseAuth.getInstance().currentUser?.reload()
        _isEmailVerified.value = FirebaseAuth.getInstance().currentUser?.isEmailVerified == true
    }

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signUpState.send(RegisterState(isError = result.message))
                }

                is Resource.Loading -> {
                    _signUpState.send(RegisterState(isLoading = true))
                }

                is Resource.Success -> {
                    _signUpState.send(RegisterState(isSuccess = "Register Success"))
                }
            }
        }
    }

    private val _registerState = MutableStateFlow(RegisterDetails())
    val registerState = _registerState.asStateFlow()

    fun updateUiState(registerDetails: RegisterDetails) {
        _registerState.value = registerDetails
    }

    suspend fun addUserDetail(name: String, address: String, image: ByteArray) {
        repository.addUserDetail(name, address, image).collect {
            when (it) {
                is Resource.Error -> {
                    _updateDetailStatus.send(LoginStatus(isError = it.message))
                }

                is Resource.Loading -> {
                    _updateDetailStatus.send(LoginStatus(isLoading = true))

                }

                is Resource.Success -> {
                    _updateDetailStatus.send(LoginStatus(isSuccess = it.data))
                }
            }
        }
    }
}


fun handleImageCapture(bitmap: Bitmap): ByteArray {
    return convertBitmapToByteArray(bitmap)
}

fun handleImageSelection(uri: Uri, context: Context): ByteArray {
    val bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media
            .getBitmap(context.contentResolver, uri)

    } else {
        val source = ImageDecoder
            .createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
    return convertBitmapToByteArray(bitmap)
}
fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}
data class LoginStatus(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)