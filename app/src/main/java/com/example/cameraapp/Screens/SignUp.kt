package com.example.cameraapp.Screens

import CustomizableGradientText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.Components.GradientButton
import com.example.cameraapp.Components.GradientTextField
import com.example.cameraapp.Components.LineWithText
import com.example.cameraapp.FirebaseService
import com.example.cameraapp.Navigation.AUTH_ROUTE
import com.example.cameraapp.Navigation.MAIN_ROUTE
import com.example.cameraapp.Navigation.ScreenSealed
import com.example.cameraapp.R
import com.example.cameraapp.ui.theme.Montserrat
import com.google.firebase.auth.userProfileChangeRequest

@Composable
fun SignUp(navHostController: NavHostController){
    val gradient = Brush.horizontalGradient(
        colorStops =  arrayOf(
            0.0f to Color(0xFF6AC4ED),
            1.0f to Color(0xFF4285B7)
        )
    )

    val email = remember{
        mutableStateOf("")
    }

    val password = remember{
        mutableStateOf("")
    }

    val name = remember {
        mutableStateOf("")
    }

    val showPassword = remember{
        mutableStateOf(false)
    }

    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterName = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()
    val emailScrollState = rememberScrollState()
    val passwordScrollState = rememberScrollState()
    val nameScrollState = rememberScrollState()

    //val bottomTextGradient = Brush.
    Box(modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(R.drawable.auth_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)

        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally)
        {
            Spacer(modifier = Modifier.height(120.dp))
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "CREATE \n" +
                            "AN \n" +
                            "ACCOUNT",
                    modifier = Modifier.align(Alignment.Center),

                    fontSize = 38.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 3.2.sp,
                    color = Color.White,
                    style = TextStyle(
                        drawStyle = Stroke(width = 0.2f)
                    )
                )
                Text(
                    text = "CREATE \n" +
                            "AN \n" +
                            "ACCOUNT",
                    fontFamily = Montserrat,
                    textAlign = TextAlign.Center,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.2.sp,
                    style = TextStyle(
                        brush = gradient
                    )
                )
            }

            Row(modifier = Modifier.fillMaxWidth().padding(start = 30.dp, top = 60.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Username",
                        fontSize = 13.sp,
                        fontFamily = Montserrat,
                        color = Color(0xFFFCFCFC),
                        letterSpacing = 1.04.sp,
                        fontWeight = FontWeight.Thin,
                        style = TextStyle(
                            drawStyle = Stroke(width = 0.2f)
                        )
                    )
                    Text(
                        text = "Username",
                        fontSize = 13.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Thin,
                        color = Color.White,
                        letterSpacing = 1.04.sp
                    )
                }
            }


            GradientTextField(
                22.dp,
                10.dp,
                name,
                focusRequesterName,
                focusRequesterEmail,
                keyboardController!!,
                nameScrollState,
                "name",
                showPassword
            )

            Row(modifier = Modifier.fillMaxWidth().padding(start = 30.dp, top = 18.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "E-mail",
                        fontSize = 13.sp,
                        fontFamily = Montserrat,
                        color = Color(0xFFFCFCFC),
                        letterSpacing = 1.04.sp,
                        fontWeight = FontWeight.Thin,
                        style = TextStyle(
                            drawStyle = Stroke(width = 0.2f)
                        )
                    )
                    Text(
                        text = "E-mail",
                        fontSize = 13.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Thin,
                        color = Color.White,
                        letterSpacing = 1.04.sp
                    )
                }
            }

            GradientTextField(22.dp,10.dp,email,focusRequesterEmail,focusRequesterPassword,keyboardController!!,emailScrollState,"email",showPassword)

            Row(modifier = Modifier.fillMaxWidth().padding(start = 30.dp, top = 18.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Password",
                        fontSize = 13.sp,
                        fontFamily = Montserrat,
                        color = Color(0xFFFCFCFC),
                        letterSpacing = 1.04.sp,
                        fontWeight = FontWeight.Thin,
                        style = TextStyle(
                            drawStyle = Stroke(width = 0.2f)
                        )
                    )
                    Text(
                        text = "Password",
                        fontSize = 13.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Thin,
                        color = Color.White,
                        letterSpacing = 1.04.sp
                    )
                }
            }

            GradientTextField(
                22.dp,
                10.dp,
                password,
                focusRequesterPassword,
                focusRequesterPassword,
                keyboardController,
                passwordScrollState,
                "password",
                showPassword
            )






            Spacer(modifier = Modifier.height(50.dp))
            GradientButton("SIGN UP", onClick = {
                FirebaseService.firebaseAuth.createUserWithEmailAndPassword(email.value,password.value)
                    .addOnSuccessListener {
                        val user = FirebaseService.firebaseAuth.currentUser
                        user?.let {
                            val profileUpdates = userProfileChangeRequest {
                                displayName = name.value
                            }
                            it.updateProfile(profileUpdates).addOnSuccessListener {
                                navHostController.navigate(MAIN_ROUTE){
                                    popUpTo(AUTH_ROUTE)
                            }
                        }
                    }
                    }
            })
            Spacer(modifier = Modifier.height(40.dp))

            LineWithText()

            Spacer(modifier = Modifier.height(30.dp))

            CustomizableGradientText(
                text = "SIGN UP WITH",
                fontSize = 14.sp,
                letterSpacing = 0.08.em, // 8%
                gradientStartColor = Color(0xFFF2FBFF),
                gradientEndColor = Color(0xFFB3D4ED),
                dropShadowColor = Color(0xFF00B7FF),
                dropShadowBlurRadius = 1.4f,
                dropShadowOffsetX = 0f,
                dropShadowOffsetY = 0f,
                strokeColor = Color(0xFFFCFCFC), // Figma stroke color (for reference)
                strokeWeight = 0.2f, // Figma stroke weight (for reference)
                // Example of changing font family and font weight
                fontFamily = Montserrat, // Replace with your actual imported font family
                fontWeight = FontWeight.Bold // Example font weight
            )

            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 36.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                Image(painter = painterResource(R.drawable.google), modifier = Modifier.height(30.dp).width(30.dp), contentDescription = null)
                Image(painter = painterResource(R.drawable.fb), modifier = Modifier.height(30.dp).width(30.dp), contentDescription = null)
                Image(painter = painterResource(R.drawable.x), modifier = Modifier.height(30.dp).width(30.dp), contentDescription = null)

            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                CustomizableGradientText(
                    text = "ALREADY HAVE AN ACCOUNT?",
                    fontSize = 14.sp,
                    letterSpacing = 0.08.em, // 8%
                    gradientStartColor = Color(0xFFF2FBFF),
                    gradientEndColor = Color(0xFFB3D4ED),
                    dropShadowColor = Color(0xFF00B7FF),
                    dropShadowBlurRadius = 1.4f,
                    dropShadowOffsetX = 0f,
                    dropShadowOffsetY = 0f,
                    strokeColor = Color(0xFFFCFCFC), // Figma stroke color (for reference)
                    strokeWeight = 0.2f, // Figma stroke weight (for reference)
                    // Example of changing font family and font weight
                    fontFamily = Montserrat, // Replace with your actual imported font family
                    fontWeight = FontWeight.Bold // Example font weight
                )
                Spacer(modifier = Modifier.width(6.dp))
                CustomizableGradientText(
                    clickable = true,
                    navHostController=navHostController,
                    route = ScreenSealed.Login.route,
                    name = "signup",
                    text = "LOG IN",
                    fontSize = 14.sp,
                    letterSpacing = 0.08.em, // 8%
                    gradientStartColor = Color(0xFF11A1E3),
                    gradientEndColor = Color(0xFF0D97FF),
                    dropShadowColor = Color(0xFF00B7FF),
                    dropShadowBlurRadius = 1.4f,
                    dropShadowOffsetX = 0f,
                    dropShadowOffsetY = 0f,
                    strokeColor = Color(0xFFFCFCFC), // Figma stroke color (for reference)
                    strokeWeight = 0.2f, // Figma stroke weight (for reference)
                    // Example of changing font family and font weight
                    fontFamily = Montserrat, // Replace with your actual imported font family
                    fontWeight = FontWeight.Bold // Example font weight
                )
            }

            Spacer(Modifier.height(40.dp))

        }
    }
}


@Preview
@Composable
fun SignUpPreview(){
    SignUp(rememberNavController())
}
