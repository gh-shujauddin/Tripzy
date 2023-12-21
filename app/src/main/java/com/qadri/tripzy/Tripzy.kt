package com.qadri.tripzy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.presentation.navigation.TripzyBottomNavigation
import com.qadri.tripzy.presentation.navigation.TripzyNavHost

@Composable
fun Tripzy(navController: NavHostController = rememberNavController()) {

    var defaultSelectedIndex by remember {
        mutableStateOf(0)
    }
//    Scaffold(
//        bottomBar = {
//            TripzyBottomNavigation(
//                navController = navController,
//                list = bottomNavigationItems,
//                defaultSelectedIndex = defaultSelectedIndex
//            )
//        }
//    ) {
        TripzyNavHost(
            navController = navController,
//            modifier = Modifier
//                .padding(top = it.calculateTopPadding())
//                .fillMaxSize(),
            bottomNavChange = { index ->
                defaultSelectedIndex = index
            }
        )
//    }

}

