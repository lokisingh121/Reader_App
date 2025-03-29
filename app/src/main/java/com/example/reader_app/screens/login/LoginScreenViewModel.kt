package com.example.reader_app.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reader_app.model.Muser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

//view model for login screen
class LoginScreenViewModel:ViewModel() {

//     we can use this but not using this so enum iske liye jo banaya hai vo faltu ho gya
//    val loadingState = MutableStateFlow(LoadingState.IDEl)

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean> = _loading

//    we use view modelscope so that i run on different thread than the main thread
    fun SigninWithEmailAndPassword(email:String,password:String,home:() -> Unit) = viewModelScope.launch{

        try {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if (task.isSuccessful){
//                    take to home after login
                    home()

                }else{

//                    asdjfklasj
                }

            }
        }catch (ex:Exception){}

    }

    fun CreateUserWithEmailAndPassword(email:String,password: String,home: () -> Unit){

        if(_loading.value == false){

            _loading.value = true

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->

                if (task.isSuccessful){

//                    i only require the test @gmail.com
                    val displayName = task.result.user?.email?.split("@")?.get(0)

                   CreateUser(displayName)
                    home()
                }else{
                    Log.d("not successful in creating account","not complete in crating account")
                }
                 _loading.value = false
            }
        }


    }

    private fun CreateUser(displayName: String?) {

        val userID = auth.currentUser?.uid

//        for creating user it hashmap is required
        val user = Muser(userID = userID.toString(), displayName = displayName.toString(), avatarUrl = "", quote = "i am the best", profession = "Android developer", id = null.toString()).toMap()



        FirebaseFirestore.getInstance().collection("users").add(user)

    }


}