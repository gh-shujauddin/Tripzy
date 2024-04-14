package com.qadri.tripzy.presentation.plan.tripDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qadri.tripzy.ui.theme.white

@Composable
fun CustomMarker(text: String) {
    Box(
        modifier = Modifier
            .height(55.dp)
            .width(85.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp)
                .rotate(45f)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp))
                .border(
                    2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            white
                        )
                    )
                ),

            )

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}