package com.qadri.tripzy.presentation.auth

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

    fun saveUser(name: String, latitude: Double, longitude: Double) = viewModelScope.launch {
        repository.addUserDetail(name, latitude, longitude).collect{ result ->
            when(result) {
                is Resource.Error -> {
                    _saveUserState.send(SaveUserDetail(isError = result.message))
                }
                is Resource.Loading -> {
                    _saveUserState.send(SaveUserDetail(isLoading = true))
                }
                is Resource.Success -> {
                    _saveUserState.send(SaveUserDetail(isSuccess = "Details updated"))
                }
            }
        }
    }

    private val _registerState = MutableStateFlow(RegisterDetails())
    val registerState = _registerState.asStateFlow()

    fun updateUiState(registerDetails: RegisterDetails) {
        _registerState.value = registerDetails
    }

    fun addUserDetail(name: String, latitude: Double, longitude: Double) {
        repository.addUserDetail(name, latitude, longitude)
    }

    private var _registerResponse = MutableStateFlow(RegisterResponse())
    val registerResponse = _registerResponse.asStateFlow()

    private val _searchAutoComplete = MutableStateFlow<ApiResult<LocationResult>>(ApiResult.Loading())
    val searchAutoComplete = _searchAutoComplete.asStateFlow()


    fun fetchSearchAutoComplete(location: String) {
        viewModelScope.launch {
            repository.getSearchAutoComplete(string = location)
                .flowOn(Dispatchers.Default)
                .catch {
                    _searchAutoComplete.value = ApiResult.Error(it.message ?: "Something went wrong")
                }
                .collect{
                    _searchAutoComplete.value = it
                }

        }
    }
}