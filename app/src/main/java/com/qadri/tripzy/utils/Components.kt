package com.qadri.tripzy.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.qadri.tripzy.R
import com.qadri.tripzy.constants.placesList

@Composable
fun NonlazyGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable() (Int) -> Unit
) {
    Column(modifier = modifier) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}


const val DEFAULT_MINIMUM_TEXT_LINE = 5

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    text: String,
    collapsedMaxLine: Int = DEFAULT_MINIMUM_TEXT_LINE,
    showMoreText: String = "... Show More",
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    showLessText: String = " Show Less",
    showLessStyle: SpanStyle = showMoreStyle,
    textAlign: TextAlign? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .noRippleClickable {
            isExpanded = !isExpanded
        }
        .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(style = showLessStyle) { append(showLessText) }
                    } else {
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        withStyle(style = showMoreStyle) { append(showMoreText) }
                    }
                } else {
                    append(text)
                }
            },
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            textAlign = textAlign
        )
    }

}


@Composable
fun CardsRow() {
    val categories = placesList
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { item ->
            Card(
                modifier = Modifier
                    .width(250.dp)
                    .padding(horizontal = 4.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        Modifier
                            .padding(8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .height(150.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.link)
                                .crossfade(true)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = null,
                            placeholder = painterResource(id = R.drawable.lake),
                            error = painterResource(id = R.drawable.ic_error_image_generic),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                            Row(modifier = Modifier) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(top = 4.dp, end = 4.dp)
                                        .size(20.dp)
                                )
                                Text(text = item.place, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Box {
                            Icon(
                                imageVector = if (item.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                contentDescription = "Bookmark",
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingCircularProgressIndicator() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector? = null,
) {
    AlertDialog(
        icon = {
            if (icon != null)
                Icon(icon, contentDescription = "Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}