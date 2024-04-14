package com.qadri.tripzy.presentation.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qadri.tripzy.R
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ConfirmEmailDestination : NavigationDestination {
    override val route: String = "confirm_email"
    override val titleRes: Int = R.string.email_confirmation
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmEmailScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onEmailVerify: () -> Unit
) {
    Log.d("ConfirmEmail", "Reaching")

    val isEmailVerified = viewModel.isEmailVerified.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        viewModel.reloadEmailVerified()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(Modifier.padding(horizontal = 24.dp)) {
                IconButton(
                    onClick = onNavigateUp
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Navigate Up",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Please click on the link sent on your email to verify.",
                    style = MaterialTheme.typography.displayMedium
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.confirm_email_not_reached),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = stringResource(id = R.string.resend_email),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { viewModel.sendConfirmationEmail() }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "You will be automatically redirected once confirmed.",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            LaunchedEffect(key1 = true) {
                scope.launch {
                    while (true) {
                        delay(3000L)
                        viewModel.reloadEmailVerified()
                        if (isEmailVerified.value) {
                            onEmailVerify()
                            val success = "Email verified."
                            Toast.makeText(context, success, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}