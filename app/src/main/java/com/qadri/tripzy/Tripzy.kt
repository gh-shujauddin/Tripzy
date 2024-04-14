package com.qadri.tripzy

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.qadri.tripzy.presentation.navigation.TripzyNavHost

@Composable
fun Tripzy(navController: NavHostController = rememberNavController()) {
        TripzyNavHost(
            navController = navController,
            context = LocalContext.current
        )

}

