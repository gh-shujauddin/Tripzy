package com.qadri.tripzy.presentation.plan.tripDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.qadri.tripzy.presentation.plan.PlanViewModel
import com.qadri.tripzy.ui.theme.dark
import com.qadri.tripzy.utils.base64ToByteArray
import com.qadri.tripzy.utils.convertImageByteArrayToBitmap


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreInfoTrips(viewModel: PlanViewModel) {
    val cardData1 = listOf(
        GridCardData(
            topText = "Day",
            bottomText = viewModel.currentDay.value,
            icon = Icons.Filled.Public
        ),
        GridCardData(
            topText = "Time",
            bottomText = viewModel.currentTimeOfDay.value,
            icon = Icons.Filled.Public
        ),
    )
    val coroutineScope = rememberCoroutineScope()
    val modalSheetStates = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88f)
            .background(MaterialTheme.colorScheme.tertiary)
    ) {


        var dayTrips =
            viewModel.getMoreInfo(
                destination = viewModel.currentNewDestination.value
            ).collectAsState(initial = emptyList())



        if (dayTrips.value.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Column {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                    dayTrips.value[0]?.photoBase64?.let {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.34f),
                            shape = RoundedCornerShape(0.dp),
                            elevation = CardDefaults.cardElevation(7.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            convertImageByteArrayToBitmap(base64ToByteArray(it))?.asImageBitmap()
                                ?.let { it1 ->
                                    Image(
                                        bitmap = it1,
                                        contentDescription = "some useful description",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                            .drawWithCache {
                                                val gradient = Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Black.copy(0.8f)
                                                    ),
                                                    startY = size.height / 5.5f,
                                                    endY = size.height
                                                )
                                                onDrawWithContent {
                                                    drawContent()
                                                    drawRect(
                                                        gradient,
                                                        blendMode = BlendMode.Multiply
                                                    )
                                                }
                                            },
                                        contentScale = ContentScale.Crop
                                    )
                                }
                        }
                    }

                }
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = dayTrips.value[0]?.name ?: "Destination",
                        color = dark,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = dayTrips.value[0]?.budget ?: " Undefined", color = dark)
//                    Text(text = dayTrips.value[0]?.toString() ?: "")
                }
            }
        }
    }
}
