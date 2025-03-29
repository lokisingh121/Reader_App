package com.example.reader_app.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.reader_app.components.EmailInput
import com.example.reader_app.components.Readerlogo
import com.example.reader_app.components.passwordInput
import com.example.reader_app.navigations.ReaderScreens

@Composable
fun ReaderLoginScreen(navController: NavController,viewModel: LoginScreenViewModel = viewModel()) {

    val  showLoginForm = rememberSaveable() { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFf9c58d),
                            Color(0xFFf492f0)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(150.dp))
                Readerlogo(modifier = Modifier)

//              checking account is there or not

                if(showLoginForm.value){
                    UserForm(loading = false,isCreateAccount = false){ email,password->

//                        Log.d(" login check" , "$email , $password")

//                        here we are checking if user in present in firebase or not
                        viewModel.SigninWithEmailAndPassword(email,password){

//                           if task is succcessful we are going home screen
                         navController.navigate(ReaderScreens.HomeScreen.name)
                        }

                    }
                }else{UserForm(loading = false,isCreateAccount = true){ email,password->



                    viewModel.CreateUserWithEmailAndPassword(email, password){
                        navController.navigate(ReaderScreens.HomeScreen.name)
                    }



                }}




                Spacer(modifier = Modifier.height(5.dp))

                Row (modifier = Modifier.padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){

                    Text("New User?")

                    val text = if(showLoginForm.value) "Sign up" else "Login"
                    Text(text, modifier = Modifier.padding(start = 5.dp).clickable { showLoginForm.value = !showLoginForm.value }, color = Color.Blue)

                }
            }





        }
    }

}




@Composable
fun UserForm(loading: Boolean=false,
             isCreateAccount:Boolean=false,
             onDone:(String,String) -> Unit ={email,pwd -> }){



    val email = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable() { mutableStateOf("") }

    val PasswordVisibility = rememberSaveable() { mutableStateOf(false) }

    val valid = remember(email.value,password.value) { email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()  }

    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val modifier = Modifier.verticalScroll(
        rememberScrollState()
    )

    Column (modifier=modifier, horizontalAlignment = Alignment.CenterHorizontally ){

        if(isCreateAccount) Text(" Place enter a valid email and password ", modifier = Modifier.padding(4.dp))


        EmailInput(emailState = email,enbled = !loading, onAction = KeyboardActions{passwordFocusRequester.requestFocus()})

        passwordInput(
            modifier = Modifier.focusRequester(passwordFocusRequester),
            passwordState = password,
            enabled = !loading,
            labelId = "password",
            passwordVisibility = PasswordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions onDone(
                    email.value.trim(),
                    password.value.trim()
                )
            }

        )

            Spacer(modifier = Modifier.height(50.dp))

        SubmiButton(loading = loading, textID = if(isCreateAccount) "Create Account" else "Login",validInputs = valid ){

            onDone(email.value,password.value)
            keyboardController?.hide()
        }

    }




}

@Composable
fun SubmiButton(loading: Boolean, textID: String, validInputs: Boolean,onClick: ()->Unit) {

    Button(onClick =onClick, modifier = Modifier.padding(3.dp).fillMaxWidth(), enabled = !loading && validInputs, shape = CircleShape,

    ) {

        if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp)) else Text(textID, modifier = Modifier.padding(5.dp))


    }



}









