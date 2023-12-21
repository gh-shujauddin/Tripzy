package com.qadri.tripzy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.data.BottomNavigationScreens
import com.qadri.tripzy.presentation.search.SearchDestination
import com.qadri.tripzy.presentation.search.SearchScreen
import com.qadri.tripzy.presentation.account.AccountDestination
import com.qadri.tripzy.presentation.account.AccountScreen
import com.qadri.tripzy.presentation.home.HomeDestination
import com.qadri.tripzy.presentation.home.HomeScreen
import com.qadri.tripzy.presentation.plan.PlanDestination
import com.qadri.tripzy.presentation.plan.PlanScreen

@Composable
fun TripzyNavHost(
    navController: NavHostController,
    bottomNavChange: (Int) -> Unit
) {

    var nav by remember {
        mutableStateOf("home")
    }
    NavHost(navController = navController, startDestination = HomeDestination.route) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navController = navController,
                onSearchClick = {
                    navController.navigate("${SearchDestination.route}/$it") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBottomItemClick = {
                    nav = bottomNavigationItems[it].route
                    val f = false
                    if (bottomNavigationItems[it] == BottomNavigationScreens.Search) {
                        nav = "${nav}/$f"
                    }
                    navController.navigate(nav) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(
            route = SearchDestination.routeWithArgs,
            arguments = listOf(navArgument(SearchDestination.isFocusedArg) {
                type = NavType.BoolType
            })
        ) {
            SearchScreen(
                navController = navController,
                onBottomItemClick = {
                    nav = bottomNavigationItems[it].route
                    val f = false
                    if (bottomNavigationItems[it] == BottomNavigationScreens.Search) {
                        nav = "${nav}/$f"
                    }
                    navController.navigate(nav) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = PlanDestination.route) {
            PlanScreen(
                navController = navController,
                onBottomItemClick = {
                    nav = bottomNavigationItems[it].route
                    val f = false
                    if (bottomNavigationItems[it] == BottomNavigationScreens.Search) {
                        nav = "${nav}/$f"
                    }
                    navController.navigate(nav) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

            )
        }
        composable(route = AccountDestination.route) {
            AccountScreen(
                navController = navController,
                onBottomItemClick = {
                    nav = bottomNavigationItems[it].route
                    val f = false
                    if (bottomNavigationItems[it] == BottomNavigationScreens.Search) {
                        nav = "${nav}/$f"
                    }
                    navController.navigate(nav) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

            )
        }
    }
}