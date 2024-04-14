package com.qadri.tripzy.presentation.plan.tripDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qadri.tripzy.ui.theme.dark


@Composable
fun CollapsedTopBarDetailsScreen(
    text: String,
    isCollapsed: Boolean,
    navController: NavController
) {
    AnimatedVisibility(
        visible = isCollapsed,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(modifier = Modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(dark.copy(0.5f))
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIos,
                    contentDescription = "Arrow Back",
                    tint = dark,
                    modifier = Modifier
                        .padding(start = 0.dp)
                        .size(25.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Spacer(modifier = Modifier.width(15.dp))

                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = dark,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                    ) {
                        append(text)
                    }
                }, modifier = Modifier.padding(end = 10.dp))
            }
            Divider(thickness = 1.dp, color = dark.copy(0.5f))
        }
    }
}