package com.qadri.tripzy.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.qadri.tripzy.presentation.account.AccountDestination
import com.qadri.tripzy.presentation.auth.ForgetPasswordDestination
import com.qadri.tripzy.presentation.auth.LoginDestination
import com.qadri.tripzy.presentation.auth.RegisterDestination
import com.qadri.tripzy.presentation.home.HomeDestination
import com.qadri.tripzy.presentation.plan.PlanDestination
import com.qadri.tripzy.presentation.search.SearchDestination

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Explore : BottomNavigationScreens(
        route = HomeDestination.route,
        resourceId = HomeDestination.titleRes,
        icon = Icons.Filled.Home
    )

    object Search : BottomNavigationScreens(
        route = SearchDestination.route,
        resourceId = SearchDestination.titleRes,
        icon = Icons.Filled.Search
    )

    object Plan : BottomNavigationScreens(
        route = PlanDestination.route,
        resourceId = PlanDestination.titleRes,
        icon = Icons.Filled.Edit
    )

    object Account : BottomNavigationScreens(
        route = AccountDestination.route,
        resourceId = AccountDestination.titleRes,
        icon = Icons.Filled.AccountCircle
    )
}

sealed class AuthNavigationScreens(val route: String, @StringRes val resourceId: Int) {
    object Login : AuthNavigationScreens(
        route = LoginDestination.route,
        resourceId = LoginDestination.titleRes
    )

    object Register : AuthNavigationScreens(
        route = RegisterDestination.route,
        resourceId = RegisterDestination.titleRes
    )

    object ForgetPassword : AuthNavigationScreens(
        route = ForgetPasswordDestination.route,
        resourceId = ForgetPasswordDestination.titleRes
    )
}

