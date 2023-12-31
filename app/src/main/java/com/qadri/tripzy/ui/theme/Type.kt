package com.qadri.tripzy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.qadri.tripzy.R

// Set of Material typography styles to start with
val poppinsBlack = FontFamily(
    Font(R.font.poppins_black, FontWeight.Black),
    Font(R.font.poppins_semibold, FontWeight.Bold),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_medium, FontWeight.Medium)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    displayLarge = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    displayMedium = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    displaySmall = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = poppinsBlack,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
    )
)