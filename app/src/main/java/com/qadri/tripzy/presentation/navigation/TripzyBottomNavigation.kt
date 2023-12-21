package com.qadri.tripzy.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.qadri.tripzy.data.BottomNavigationScreens

@Composable
fun TripzyBottomNavigation(
    navController: NavController,
    list: List<BottomNavigationScreens>,
    defaultSelectedIndex: Int,
    onBottomItemClick:(Int) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
    ) {

        var selectedIndex by remember {
            mutableStateOf(defaultSelectedIndex)
        }

        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxSize(),
        ) {

            list.forEachIndexed { index, nav ->
                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable {
                            onBottomItemClick(index)
                        },
                    contentAlignment = Center
                ) {

                    Column(
                        if (selectedIndex == index)
                            Modifier.offset(y = (-8).dp)
                        else Modifier,
                        horizontalAlignment = CenterHorizontally,
                    ) {
                        Box(
                            Modifier
                                .background(
                                    if (selectedIndex == index) MaterialTheme.colorScheme.primary
                                    else Color.Transparent,
                                    CircleShape
                                )
                                .size(36.dp),
                            contentAlignment = Center
                        ) {
                            Icon(
                                imageVector = nav.icon,
                                null,
                                Modifier.size(24.dp),
                                tint = if (selectedIndex != index) MaterialTheme.colorScheme.primary else Color.White
                            )
                        }

                        AnimatedVisibility(selectedIndex == index) {
                            Text(
                                stringResource(id = nav.resourceId),
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}