package com.qadri.tripzy.presentation.navigation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.qadri.tripzy.constants.bottomNavigationItems
import com.qadri.tripzy.presentation.account.AccountDestination
import com.qadri.tripzy.presentation.account.AccountScreen
import com.qadri.tripzy.presentation.auth.AskDetailDestination
import com.qadri.tripzy.presentation.auth.AskDetailScreen
import com.qadri.tripzy.presentation.auth.AskLoginDestination
import com.qadri.tripzy.presentation.auth.AskLoginScreen
import com.qadri.tripzy.presentation.auth.ConfirmEmailDestination
import com.qadri.tripzy.presentation.auth.ConfirmEmailScreen
import com.qadri.tripzy.presentation.auth.ForgetPasswordDestination
import com.qadri.tripzy.presentation.auth.ForgotPasswordScreen
import com.qadri.tripzy.presentation.auth.GoogleAuthUiClient
import com.qadri.tripzy.presentation.auth.LoginDestination
import com.qadri.tripzy.presentation.auth.LoginScreen
import com.qadri.tripzy.presentation.auth.LoginViewModel
import com.qadri.tripzy.presentation.auth.RegisterDestination
import com.qadri.tripzy.presentation.auth.RegisterScreen
import com.qadri.tripzy.presentation.home.HomeDestination
import com.qadri.tripzy.presentation.home.HomeScreen
import com.qadri.tripzy.presentation.home.HomeViewModel
import com.qadri.tripzy.presentation.placeDetail.PlaceDetailDestination
import com.qadri.tripzy.presentation.placeDetail.PlaceDetailScreen
import com.qadri.tripzy.presentation.plan.NewTripDestination
import com.qadri.tripzy.presentation.plan.NewTripScreen
import com.qadri.tripzy.presentation.plan.NewTripViewModel
import com.qadri.tripzy.presentation.plan.PlanDestination
import com.qadri.tripzy.presentation.plan.PlanScreen
import com.qadri.tripzy.presentation.plan.PlanViewModel
import com.qadri.tripzy.presentation.plan.tripDetails.TripDetailDestination
import com.qadri.tripzy.presentation.plan.tripDetails.TripDetailsScreen
import com.qadri.tripzy.presentation.search.SearchDestination
import com.qadri.tripzy.presentation.search.SearchScreen
import com.qadri.tripzy.ui.SplashDestination
import com.qadri.tripzy.ui.SplashScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TripzyNavHost(
    navController: NavHostController,
    context: Context
) {
    val scope = rememberCoroutineScope()
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val firebase = FirebaseAuth.getInstance()
    val user = firebase.currentUser

    val planViewModel: PlanViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    var nav by remember {
        mutableStateOf("home")
    }

    val startDestination =
        if (user != null) {
            HomeDestination.route
        } else {
            "auth_nav"
        }
    NavHost(navController = navController, startDestination = "bottom_nav") {

        navigation(startDestination = SplashDestination.route, route = "bottom_nav") {
            composable(SplashDestination.route) {
                SplashScreen(
                    onComplete = {
                        scope.launch(Dispatchers.Main) {
                            navController.popBackStack()
                            navController.navigate(startDestination)
                        }
                    }
                )
            }
            composable(route = HomeDestination.route) {
                HomeScreen(
                    navController = navController,
                    onSearchClick = {
                        val f = true
                        navController.navigate("${SearchDestination.route}/$f") {
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
                    },
                    onItemClick = {
                        navController.navigate("${PlaceDetailDestination.route}/$it")
                    },
                    homeViewModel = homeViewModel
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
                    },
                    onCardClick = {
                        Log.d("Route", it.toString())
                        navController.navigate("${PlaceDetailDestination.route}/$it")
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
                    },
                    planViewModel = planViewModel
                )
            }
            composable(route = NewTripDestination.route) {
                val viewModel: NewTripViewModel = hiltViewModel()

                NewTripScreen(
                    viewModel = viewModel,
                    planViewModel = planViewModel,
                    navController = navController
                )
            }
            composable(TripDetailDestination.route) {
                TripDetailsScreen(
                    viewModel = planViewModel,
                    navController = navController
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
                    },
                    currentUser = googleAuthUiClient.getSignedInUser(),
                    onSignIn = {
                        navController.navigate(AskLoginDestination.route)
                    },
                    onSignOut = {
                        scope.launch {
                            googleAuthUiClient.signOut()
                        }
                        navController.navigate("auth_nav")
                        Toast.makeText(
                            context,
                            "User Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }

//            composable(route = TestDestination.route) {
//                TestScreen(
//                    navController = navController,
//                    onClick = {
//                        navController.navigate(AskLoginDestination.route)
//                    },
//                    onBottomItemClick = {
//                        nav = bottomNavigationItems[it].route
//                        val f = false
//                        if (bottomNavigationItems[it] == BottomNavigationScreens.Search) {
//                            nav = "${nav}/$f"
//                        }
//                        navController.navigate(nav) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                )
//            }

            composable(
                route = PlaceDetailDestination.routeWithArgs,
                arguments = listOf(navArgument(PlaceDetailDestination.placeIdArg) {
                    type = NavType.IntType
                })
            ) {
                PlaceDetailScreen(
                    onNavigateUp = {
                        navController.popBackStack()
                    }
                )
            }
        }

        //AuthNavigation
        navigation(startDestination = AskLoginDestination.route, route = "auth_nav") {
            composable(route = AskLoginDestination.route) {
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val currentUserStatus = viewModel.currentUserStatus.collectAsState(initial = null)
                var isGoogleSigned by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        Toast.makeText(
                            context,
                            "Sign in successful",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.resetState()
                        viewModel.getUserDetails()
                        isGoogleSigned = true
                    }
                }

                LaunchedEffect(currentUserStatus.value?.isSuccess) {
                    if (currentUserStatus.value?.isSuccess != null && isGoogleSigned) {
                        navController.navigate(HomeDestination.route)
                        Log.d("USER", currentUserStatus.value!!.isSuccess.toString())
                    }
                }

                LaunchedEffect(currentUserStatus.value?.isError) {
                    if (currentUserStatus.value?.isError != null && isGoogleSigned ) {
                        val currentUser = googleAuthUiClient.getSignedInUser()
                        if (currentUser != null) {
                            navController.navigate(AskDetailDestination.route)
                        }
                    }
                }


                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            scope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInWithGoogleResult(signInResult)
                            }
                        }
                    }
                )


                AskLoginScreen(
                    loginWithEmail = { navController.navigate(LoginDestination.route) },
                    loginWithGoogle = {
                        scope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )

                        }
                    },
                    onSkip = { navController.popBackStack() }
                )
            }
            composable(route = LoginDestination.route) {
                LoginScreen(
                    signInSuccess = { navController.navigate(HomeDestination.route) },
                    onNavigateUp = { navController.popBackStack() },
                    onRegisterButtonClicked = { navController.navigate(RegisterDestination.route) },
                    onForgotPasswordButtonClicked = {
                        navController.navigate(
                            ForgetPasswordDestination.route
                        )
                    }
                )
            }
            composable(route = RegisterDestination.route) {
                RegisterScreen(
                    registerSuccess = {
                        navController.navigate(ConfirmEmailDestination.route)
                    },
                    onLogin = { navController.popBackStack() },
                    onNavigateUp = { navController.popBackStack() })
            }

            composable(route = ForgetPasswordDestination.route) {
                ForgotPasswordScreen(
                    onNavigateUp = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = ConfirmEmailDestination.route) {
                ConfirmEmailScreen(
                    onNavigateUp = { navController.popBackStack() },
                    onEmailVerify = { navController.navigate(AskDetailDestination.route) }
                )
            }
            composable(route = AskDetailDestination.route) {
                AskDetailScreen(
                    navigateToHome = {
                        navController.navigate(HomeDestination.route)
                        Toast.makeText(context, "Details saved", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }
}