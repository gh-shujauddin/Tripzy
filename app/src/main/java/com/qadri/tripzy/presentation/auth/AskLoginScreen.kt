package com.qadri.tripzy.presentation.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qadri.tripzy.R
import com.qadri.tripzy.presentation.navigation.NavigationDestination

object AskLoginDestination : NavigationDestination {
    override val route: String = "ask_for_login"
    override val titleRes: Int = R.string.ask_for_login
}

@Composable
fun AskLoginScreen(
    loginWithEmail: () -> Unit,
    loginWithGoogle: () -> Unit,
    onSkip: () -> Unit
) {
    Log.d("Ask", "Reaching")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onSkip) {
                Text(text = "Skip", textDecoration = TextDecoration.Underline)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .size(70.dp)
                .background(Color.LightGray)
                .padding(8.dp)
        ) {
            //App Icon
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Log in to start planning your trip.",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    ParagraphStyle(
                        lineHeight = 14.sp
                    )
                ) {
                    withStyle(
                        SpanStyle(
                            fontSize = 10.sp,
                        )
                    ) {
                        append("By Proceeding, you agree to our ")
                    }
                    withStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    ) {
                        append("Terms of Use ")
                    }
                    withStyle(
                        SpanStyle(
                            fontSize = 10.sp
                        )
                    ) {
                        append("and confirm you have read our ")
                    }
                    withStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    ) {
                        append("Privacy and Cookie Statement.")
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = loginWithGoogle, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = "Google icon",
                modifier = Modifier.size(25.dp)
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                Text(text = "Continue with Google")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = loginWithEmail, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Sharp.Email,
                contentDescription = "Email icon",
                modifier = Modifier.size(25.dp)
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                Text(text = "Continue with email")
            }
        }
    }
}

@Preview
@Composable
fun LoginAskPrev() {
    AskLoginScreen(loginWithEmail = { /*TODO*/ }, loginWithGoogle = {}) {

    }
}
