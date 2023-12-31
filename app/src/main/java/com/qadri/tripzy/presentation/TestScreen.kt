package com.qadri.tripzy.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.presentation.auth.AskLoginScreen
import com.qadri.tripzy.presentation.auth.LoginDestination
import com.qadri.tripzy.presentation.auth.LoginScreen
import com.qadri.tripzy.presentation.auth.RegisterDestination
import com.qadri.tripzy.presentation.navigation.BottomNavigationScreens
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import com.qadri.tripzy.presentation.navigation.TripzyBottomNavigation

object TestDestination : NavigationDestination {
    override val route: String
        get() = "test"
    override val titleRes: Int
        get() = R.string.test

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TestScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    defaultSelectedIndex: Int = bottomNavigationItems.indexOf(BottomNavigationScreens.Text),
    onClick: () -> Unit,
    onBottomItemClick: (Int) -> Unit
) {
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
//        AskLoginScreen(
//            loginWithEmail = { navController.navigate(LoginDestination.route) },
//            loginWithGoogle = {},
//            onSkip = { navController.navigateUp() }
//        )
        Button(onClick = onClick) {
            Text(text = "Button")
        }
    }
}