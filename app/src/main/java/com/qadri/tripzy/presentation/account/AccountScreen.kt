package com.qadri.tripzy.presentation.account

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NextPlan
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.domain.SignInState
import com.qadri.tripzy.domain.UserData
import com.qadri.tripzy.presentation.auth.LoginViewModel
import com.qadri.tripzy.presentation.navigation.BottomNavigationScreens
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.navigation.TripzyBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object AccountDestination : NavigationDestination {
    override val route: String
        get() = "account"
    override val titleRes: Int
        get() = R.string.account
}

@Composable
fun AccountListCard(icon: ImageVector, text: String, onClick: () -> Unit) {

    Column {
        Row {
            Icon(imageVector = icon, contentDescription = null)
            Row {
                Text(text = text)
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Click here"
                )
            }
        }
        Divider()
    }
}

@Composable
fun AccountScreen(
    defaultSelectedIndex: Int = bottomNavigationItems.indexOf(BottomNavigationScreens.Account),
    navController: NavController,
    onBottomItemClick: (Int) -> Unit,
    currentUser: UserData?,
    onSignOut: () -> Unit,
    onSignIn: () -> Unit
) {
    val signInViewModel: LoginViewModel = hiltViewModel()
    Scaffold(
        bottomBar = {
            TripzyBottomNavigation(
                navController = navController,
                list = bottomNavigationItems,
                defaultSelectedIndex = defaultSelectedIndex,
                onBottomItemClick = onBottomItemClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 12.dp, end = 12.dp, top = it.calculateTopPadding())
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = AccountDestination.titleRes))
                Box {

                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column {
                AccountListCard(icon = Icons.Filled.NextPlan, text = "Plans") {

                }
                AccountListCard(icon = Icons.Filled.AccountCircle, text = "Profile") {

                }
                AccountListCard(icon = Icons.Filled.Settings, text = "Preferences") {

                }
                AccountListCard(icon = Icons.Filled.ContactSupport, text = "Support") {

                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SignButton(
                isSigned = currentUser != null,
                onClick = {
                    if (currentUser != null) {
                        onSignOut()
                    } else {
                        onSignIn()
                    }
                }
            )
        }
    }
}

@Composable
fun SignButton(isSigned: Boolean, onClick: () -> Unit) {
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    OutlinedButton(
        onClick = {
            isTimerRunning = true
            onClick()

        },
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isTimerRunning)
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        else
            Text(text = if (isSigned) "Sign out" else "Sign in")
    }
}
