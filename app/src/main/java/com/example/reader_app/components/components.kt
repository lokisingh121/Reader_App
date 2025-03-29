package com.example.reader_app.components

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reader_app.R
import com.example.reader_app.model.Mbooks
import com.example.reader_app.navigations.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Readerlogo(modifier: Modifier) {
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







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(modifier: Modifier=Modifier,
               emailState: MutableState<String>,
               labelId:String = "Email",
               enbled:Boolean=true,
               imeAction  : ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default
){

    InputField(modifier=modifier,
        valueState = emailState,
        lableID = labelId,
        enabledd = enbled,
        imeAction = imeAction,
        oneAction = onAction,
        keyboardType = KeyboardType.Email)
}

@Composable
fun InputField(modifier: Modifier=Modifier,
               valueState : MutableState<String>,
               lableID:String,
               enabledd:Boolean,
               isSingleLine:Boolean = true,
               keyboardType: KeyboardType = KeyboardType.Text,
               imeAction : ImeAction = ImeAction.Next,
               oneAction: KeyboardActions = KeyboardActions.Default,
               backgroundcolor:Color = MaterialTheme.colorScheme.background){

    val textcolor = contentColorFor(backgroundcolor)

    OutlinedTextField(value = valueState.value, onValueChange = {valueState.value = it},
        label = { Text(lableID) }, singleLine = isSingleLine, textStyle = TextStyle(fontSize = 18.sp),
        modifier = modifier.padding(bottom = 10.dp,start=10.dp, top = 10.dp).fillMaxWidth(),
        enabled = enabledd, keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction,
        ), keyboardActions = oneAction
    )



}



@Composable
fun passwordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    enabled: Boolean,
    labelId: String,
    passwordVisibility: MutableState<Boolean>,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Done ) {

    val visualTransformatioin = if(passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()

    OutlinedTextField(value  = passwordState.value, onValueChange = {passwordState.value = it},
        label = { Text(labelId) }, singleLine = true, modifier = Modifier.padding(bottom = 1.dp, start = 10.dp, top = 10.dp).fillMaxWidth(),
        enabled = enabled, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        visualTransformation = visualTransformatioin, trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)}, keyboardActions = onAction)

}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {

    val visible = passwordVisibility

    IconButton(onClick = {passwordVisibility.value = !visible.value}) {
        Icons.Default.Close
    }



}

@Composable
fun TitleSection( modifier: Modifier=Modifier,label:String ){
    Surface(modifier = modifier.padding(start = 5.dp, top = 2.dp)) {
        Column { Text(text = label, fontSize = 19.sp, textAlign = TextAlign.Left) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderAppBar(
    title:String, icon: ImageVector? =null, ShowProfile:Boolean = true, navController: NavController,
    onBackArrowClicked:()->Unit){

    TopAppBar(title = {
        Row (verticalAlignment = Alignment.CenterVertically){
            if(ShowProfile){ Icon(imageVector = Icons.Default.Person, contentDescription = "person login", modifier = Modifier.clip(
                RoundedCornerShape(12.dp)
            ).scale(0.6f).size(50.dp))}

                if(icon!=null){

                    Icon(imageVector =icon, contentDescription = "arrow back", modifier = Modifier.clickable { onBackArrowClicked() } )

                }

                    Spacer(modifier = Modifier.width(20.dp))
                Text(title, modifier = Modifier, fontWeight = FontWeight.Bold)

        }


    }, actions = {
//       it contain back button` after logout this code under run block will run

        IconButton(onClick = {
            FirebaseAuth.getInstance().signOut().run {
            navController.navigate(ReaderScreens.LoginScreen.name)
        }}) {
            if (ShowProfile){
                Column {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back button", modifier = Modifier, tint = Color.White)


                }}

        }




    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary, // Background color
        titleContentColor = MaterialTheme.colorScheme.onPrimary ,))
}


@Composable
fun FABContent(OnTap:()->Unit) {
    FloatingActionButton(onClick = {OnTap()}, shape = RoundedCornerShape(50.dp), containerColor = Color(0xff92cdbf)) {

        Icon(imageVector = Icons.Default.Add, contentDescription = "Add reading", tint = MaterialTheme.colorScheme.onSecondary)
    }

}



@Composable
fun BookRating(score: Double=4.5) {

    Surface(modifier = Modifier.height(70.dp).padding(0.dp).width(28.dp), shape = RoundedCornerShape(50.dp), shadowElevation = 6.dp) {

        Text(score.toString(), modifier = Modifier.padding(bottom = 2.dp))

        Icon(imageVector = Icons.Default.Star, contentDescription = "star" , modifier = Modifier.size(1.dp))



    }

}


@Composable
fun RounderButton(label:String,
                  radius:Int=29,
                  onPress: ()->Unit){

    Surface(modifier = Modifier.clip(RoundedCornerShape(20.dp)).clickable { onPress() }, shadowElevation = 1.dp, color = Color(0xff92cdbf)) {

        Column (modifier = Modifier.width(90.dp).heightIn(40.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){

            Text(text = label, fontSize = 15.sp)
        }
    }

}


@Composable
fun ListCard(book: Mbooks, onPressDetails:(String)->Unit){


//    we are making our column little dynamic means it will change size according to content so we need all these three things
    val context = LocalContext.current
    val resources = context.resources
    val displayMAtrics = resources.displayMetrics

    val screenWidth = displayMAtrics.widthPixels/displayMAtrics.density
    val spacing = 10.dp

    Card (modifier = Modifier.padding(10.dp).height(250.dp).width(198.dp).clickable { onPressDetails.invoke(book.tittl.toString()) },shape = RoundedCornerShape(29.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray), // Set the background color
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Set the elevation
    ){

        Column (modifier = Modifier.width(screenWidth.dp - (spacing * 2)), horizontalAlignment = Alignment.Start){

            Row (horizontalArrangement = Arrangement.Start){

//                we are getting image from url we used coil here
                Image(painter = rememberAsyncImagePainter(model = book.photourl.toString()), contentDescription = "book reading", modifier = Modifier.height(140.dp).width(100.dp).padding(4.dp))

                Spacer(modifier = Modifier.width(50.dp))
                Column (modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "fav icon", modifier = Modifier.padding(1.dp),
                        Color.Red
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BookRating(score = 3.5)
                }
            }


            Text(text = book.tittl.toString(), modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, maxLines = 1,fontSize = 16.sp)
            Text(text = book.author.toString(),modifier = Modifier.padding(4.dp),maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 14.sp)


            val isstartedreading = remember { mutableStateOf(false) }

            Row (modifier = Modifier.padding(6.dp).fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom ){

                isstartedreading.value = book.startedReading!=null
                RounderButton(label = if(isstartedreading.value) "Reading" else "Not Yet", radius = 70) { }
            }


        }



    }
}


//rating bar
//@ExperimentalMaterial3Api
//@Composable
//fun RatingBar(
//    modifier: Modifier = Modifier,
//    rating: Int,
//    onPressRating: (Int) -> Unit
//) {
//    var ratingState by remember {
//        mutableStateOf(rating)
//    }
//
//    var selected by remember {
//        mutableStateOf(false)
//    }
//    val size by animateDpAsState(
//        targetValue = if (selected) 42.dp else 34.dp,
//        spring(Spring.DampingRatioMediumBouncy)
//    )
//
//    Row(
//        modifier = Modifier.width(280.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        for (i in 1..5) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_baseline_star_24),
//                contentDescription = "star",
//                modifier = modifier
//                    .width(size)
//                    .height(size)
//                    .pointerInteropFilter {
//                        when (it.action) {
//                            MotionEvent.ACTION_DOWN -> {
//                                selected = true
//                                onPressRating(i)
//                                ratingState = i
//                            }
//                            MotionEvent.ACTION_UP -> {
//                                selected = false
//                            }
//                        }
//                        true
//                    },
//                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
//            )
//        }
//    }
//}

