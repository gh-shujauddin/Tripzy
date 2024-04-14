package com.qadri.tripzy.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.qadri.tripzy.data.TripzyRepository
import com.qadri.tripzy.domain.LoginDetails
import com.qadri.tripzy.domain.LoginResponse
import com.qadri.tripzy.domain.Resource
import com.qadri.tripzy.domain.SignInResult
import com.qadri.tripzy.domain.SignInState
import com.qadri.tripzy.domain.SignInState1
import com.qadri.tripzy.presentation.account.CurrentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: TripzyRepository
) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState1())
    val state = _state.asStateFlow()

    private val _currentUser = MutableStateFlow(FirebaseAuth.getInstance().currentUser)
    val currentUser = _currentUser.asStateFlow()


    private val _currentUserStatus = Channel<CurrentUser>()
    val currentUserStatus = _currentUserStatus.receiveAsFlow()

    fun onSignInWithGoogleResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState1() }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message))
                }

                is Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }

                is Resource.Success -> {
                    _signInState.send(SignInState(isSuccess = "Sign In Success"))
                    _loginResponse.update {
                        it.copy(email = email)
                    }
                }
            }
        }
    }

    fun getUserDetails() =
        viewModelScope.launch {

            repository.getCurrentUser().collect{ result ->
                when(result) {
                    is Resource.Error -> {
                        _currentUserStatus.send(CurrentUser(isError = result.message))
                    }

                    is Resource.Loading -> {
                        _currentUserStatus.send(CurrentUser(isLoading = true))
                    }

                    is Resource.Success -> {
                        _currentUserStatus.send(CurrentUser(isSuccess = result.data))

                    }
                }
            }
        }

    private val _loginUiState = MutableStateFlow(LoginDetails())
    val loginUiState = _loginUiState.asStateFlow()

    fun updateUiState(loginDetails: LoginDetails) {
        _loginUiState.value = loginDetails
    }

    private var _loginResponse = MutableStateFlow(LoginResponse())
    val loginResponse = _loginResponse.asStateFlow()

//    fun fetchToken() {
//        viewModelScope.launch {
//            _loginResponse.value = try {
//                repository.login(_loginUiState.value)
//            } catch (ex: ConnectException) {
//                LoginResponse(message = "Network Error")
//            }
//        }
//    }
}
