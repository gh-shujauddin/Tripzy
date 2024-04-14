package com.qadri.tripzy.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qadri.tripzy.R
import com.qadri.tripzy.presentation.navigation.NavigationDestination
import kotlinx.coroutines.delay

object SplashDestination : NavigationDestination {
    override val route: String
        get() = "splash"
    override val titleRes: Int
        get() = R.string.splash
}

@Composable
fun SplashScreen(
    onComplete: () -> Unit
) {

    val scale = remember {
        Animatable(0f)
    }
    val infiniteTransition = rememberInfiniteTransition()
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
        ), label = "rotation"
    )

    LaunchedEffect(true) {
        scale.animateTo(1f, tween(1500))
        delay(3000)
        onComplete()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(2f), verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.tripzy_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(300.dp)
                    .scale(scale.value)
                    .rotate(rotate)

            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .alpha(scale.value)
                    .scale(scale.value),
                fontSize = 32.sp,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Discover your destination!",
                modifier = Modifier
                    .alpha(scale.value)
                    .scale(scale.value),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}