package com.qadri.tripzy.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun convertImageByteArrayToBitmap(imageData: ByteArray?): Bitmap? {
    return imageData?.size?.let { BitmapFactory.decodeByteArray(imageData, 0, it) }
}

fun base64ToByteArray(base64String: String): ByteArray {
    return Base64.decode(base64String, Base64.DEFAULT)
}