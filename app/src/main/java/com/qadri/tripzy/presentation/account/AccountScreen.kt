package com.qadri.tripzy.presentation.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.qadri.tripzy.utils.AlertDialog
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
    var signOutConfirm by remember {
        mutableStateOf(false)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
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
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = AccountDestination.titleRes),
                    style = MaterialTheme.typography.displayLarge
                )
                Box {

                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column() {
                if (currentUser != null) {
                    AccountListCard(icon = Icons.Filled.NextPlan, text = "Plans", onClick = {})
                    AccountListCard(
                        icon = Icons.Filled.AccountCircle,
                        text = "Profile",
                        onClick = {}
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = .2f))
                    ) {
                        Column (modifier = Modifier.padding(12.dp)){
                            Text(
                                text = "Log in to manage and easily plan your next trip.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                            SignButton(
                                text = "Log in",
                                onClick = {
                                    onSignIn()
                                },
                                isTimerRunning = isTimerRunning,
                                modifier = Modifier.padding(
                                    bottom = 12.dp,
                                    start = 12.dp,
                                    end = 12.dp
                                )
                            )
                        }
                    }
                }
                AccountListCard(icon = Icons.Filled.Settings, text = "Preferences", onClick = {})
                AccountListCard(icon = Icons.Filled.ContactSupport, text = "Support", onClick = {})
            }
            Spacer(modifier = Modifier.height(20.dp))
            AnimatedVisibility(visible = currentUser != null) {
                SignButton(
                    text = "Sign out",
                    onClick = {
                        isTimerRunning = true
                        if (currentUser != null) {
                            signOutConfirm = true
                        }
                    },
                    isTimerRunning = isTimerRunning
                )
            }

            AnimatedVisibility(visible = signOutConfirm) {
                AlertDialog(
                    onDismissRequest = {
                        signOutConfirm = false
                        isTimerRunning = false
                    },
                    onConfirmation = { onSignOut() },
                    dialogTitle = "",
                    dialogText = "Are you sure you want to sign out?"
                )
            }
        }
    }
}

@Composable
fun SignButton(
    text: String,
    onClick: () -> Unit,
    isTimerRunning: Boolean,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = {
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        border = BorderStroke(1.7.dp, MaterialTheme.colorScheme.primary)
    ) {
        if (isTimerRunning)
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        else
            Text(text = text)
    }
}
