package com.qadri.tripzy.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qadri.tripzy.R
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import kotlinx.coroutines.launch

object LoginDestination : NavigationDestination {
    override val route: String = "login"
    override val titleRes: Int = R.string.login
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    signInSuccess: () -> Unit,
    onNavigateUp: () -> Unit,
    onForgotPasswordButtonClicked: () -> Unit,
    onRegisterButtonClicked: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState1 = viewModel.state.collectAsState().value
    val uiState = viewModel.loginUiState.collectAsStateWithLifecycle().value
    val state = viewModel.signInState.collectAsState(initial = null)
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            IconButton(
                onClick = onNavigateUp, modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .zIndex(10f)
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Navigate Up",
                    modifier = Modifier.size(30.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.cirlce_ring),
                contentDescription = "Login ring",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(100.dp)
                    .offset(x = 10.dp, y = (-5).dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_logo),
                    contentDescription = "Sample Logo",
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                Text(
                    text = stringResource(id = R.string.welcome_back),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

//                Text(
//                    text = stringResource(id = R.string.sign_in_continue),
//                    fontWeight = FontWeight.Normal,
//                    fontFamily = FontFamily.Serif,
//                    fontSize = 16.sp,
//                    color = MaterialTheme.colorScheme.primary
//                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = uiState.login,
                    onValueChange = { viewModel.updateUiState(uiState.copy(login = it)) },
                    label = {
                        Text(
                            text = stringResource(id = R.string.label_email_address),
                            style = MaterialTheme.typography.displaySmall
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.displaySmall,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))
                val visibilityIcon = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.updateUiState(uiState.copy(password = it)) },
                    label = {
                        Text(
                            text = stringResource(id = R.string.label_password),
                            style = MaterialTheme.typography.displaySmall
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Email Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.displaySmall,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {  /*on Done Logic*/
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = visibilityIcon,
                                contentDescription = description,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = stringResource(id = R.string.forgot_password),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onForgotPasswordButtonClicked() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.loginUser(uiState.login, uiState.password)
                        }
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    if (state.value?.isLoading == true) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                    } else {
                        Text(
                            text = stringResource(id = R.string.sign_in),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.background,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.do_not_have_account),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onRegisterButtonClicked() }
                    )
                }

                LaunchedEffect(key1 = state.value?.isSuccess) {
                    scope.launch {
                        if (state.value?.isSuccess?.isNotEmpty() == true) {
                            focusManager.clearFocus()
                            val success = state.value?.isSuccess
                            Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                            signInSuccess()
                        }
                    }
                }

                LaunchedEffect(key1 = state.value?.isError) {
                    scope.launch {
                        if (state.value?.isError?.isNotEmpty() == true) {
                            val error = state.value?.isError
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                LaunchedEffect(key1 = uiState1.signInError) {
                    scope.launch {
                        if (uiState1.signInError?.isNotEmpty() == true) {
                            val error = uiState1.signInError
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        signInSuccess = {},
        onNavigateUp = {},
        onRegisterButtonClicked = {},
        onForgotPasswordButtonClicked = {})
}