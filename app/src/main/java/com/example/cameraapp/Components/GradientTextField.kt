package com.example.cameraapp.Components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cameraapp.R

@Composable
fun GradientTextField(
    horizontalPadding: Dp,
    verticalPadding: Dp,
    content: MutableState<String> = mutableStateOf("hello"),
    focusRequester: FocusRequester = FocusRequester(),
    nextFocusRequester: FocusRequester = FocusRequester(),
    keyboardController: SoftwareKeyboardController = LocalSoftwareKeyboardController.current!!,
    scrollState: ScrollState = rememberScrollState(),
    name: String = "",
    showPassword:MutableState<Boolean> = mutableStateOf(false)
) {

    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color(0x5080C5E5),
            1f to Color(0x504A90C1)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .height(47.dp)
            .background(
                brush = gradient,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFBFD9E9),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        BasicTextField(
            value = content.value,
            onValueChange = { content.value = it },
            textStyle = TextStyle(color = Color.White),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 15.dp)
                .horizontalScroll(scrollState)
                .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = if (name == "email" || name=="name") ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { nextFocusRequester.requestFocus() },
                onDone = { keyboardController.hide() }
            ),
            visualTransformation = if (name == "password" && !showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (name == "password") Arrangement.SpaceBetween else Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                            .padding(start = 0.dp, end = 0.dp)
                    ) {
                        innerTextField()
                    }

                    if (name == "password") {
                        IconButton(
                            onClick = { showPassword.value = !showPassword.value },
                        ) {
                            Icon(

                                painter = if (showPassword.value) painterResource(id = R.drawable.show_pass) else painterResource(id = R.drawable.hide_pass),
                                contentDescription = if (showPassword.value) "Hide password" else "Show password",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

