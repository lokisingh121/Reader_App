package com.example.reader_app.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.navigation.NavController
import com.example.reader_app.R
import com.example.reader_app.components.Readerlogo
import com.example.reader_app.navigations.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun ReaderSplashScreen(navController: NavController ){

    val scale = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 0.9f, animationSpec = tween(durationMillis = 800, easing = {OvershootInterpolator(8f).getInterpolation(it)}))

        delay(2000L)
//        here we need to check if firebase user is there take to home screen else take to logins screen

        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(ReaderScreens.LoginScreen.name)
        }else{
            navController.navigate(ReaderScreens.HomeScreen.name)
        }

//        ye pehale daali thi
//        navController.navigate(ReaderScreens.LoginScreen.name)

    }


    Surface(
        modifier = Modifier.padding(vertical = 303.dp, horizontal = 30.dp)
            .scale(scale.value)
            .size(100.dp), // Adjust size to make it smaller and proportional
        color = Color.White,
        shape = CircleShape, border = BorderStroke(width = 1.dp, color = Color.LightGray), // Keep the circular shape
        shadowElevation = 8.dp // Optional: Adds a subtle shadow
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.readerimage),
                contentDescription = "reader image",
                modifier = Modifier
                    .padding(start = 3.dp)
                    .size(150.dp) // Keep the image smaller than the Surface
            )

            Text(
                "Read. Change. Yourself!",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )



        }
    }




}



