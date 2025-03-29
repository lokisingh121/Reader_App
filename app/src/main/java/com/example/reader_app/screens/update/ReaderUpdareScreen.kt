package com.example.reader_app.screens.update

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reader_app.R
import com.example.reader_app.components.InputField
import com.example.reader_app.components.ReaderAppBar
import com.example.reader_app.components.RounderButton
import com.example.reader_app.data.DataorException
import com.example.reader_app.model.Mbooks
import com.example.reader_app.navigations.ReaderScreens
import com.example.reader_app.screens.home.HomeScreenViewModel
import com.example.reader_app.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ReaderUpdateScreen(navController: NavController,bookItemId:String,viewModel: HomeScreenViewModel= hiltViewModel())  {

    Scaffold (topBar =

    {  ReaderAppBar(
            title = "Update book", icon = Icons.AutoMirrored.Filled.ArrowBack,
            ShowProfile = false,
            navController = navController
        ){navController.popBackStack()}}

    ){innerpadding->

        val bookInfo = produceState<DataorException<List<Mbooks>,Boolean,Exception>>(initialValue = DataorException(data = emptyList(),true,Exception(""))){

            value=viewModel.data.value
        }.value
        Surface(modifier = Modifier
            .padding(innerpadding)
            .fillMaxSize()) {

            Column (modifier = Modifier.padding(top = 3.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally){



                Log.d("INFOOO","${viewModel.data.value.data}")
            if(bookInfo.loading==true){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                bookInfo.loading=false
            }else{

                Surface(modifier = Modifier
                    .padding(17.dp)
                    .height(900.dp)
                    .width(400.dp), shadowElevation = 4.dp, color = Color(0xFF019898), shape = RoundedCornerShape(10.dp)) {

                    Column {
                        ShowBookUpdate(bookInfo=viewModel.data.value,bookItemId=bookItemId)
                        ShowSimpleForm(book= viewModel.data.value.data?.first(){ mBook->
                            mBook.googleBooksID==bookItemId
                        }!!,navController)
                    }


                }
            }
            }
        }

    }

}

@Composable
fun ShowSimpleForm(book: Mbooks, navController: NavController) {

    val context= LocalContext.current

   val  isStartedReading = remember { mutableStateOf(false) }
    val isFinshedReading = remember { mutableStateOf(false) }
    val notesText= remember { mutableStateOf("") }
    SimpleForm (defaultvalues = if(book.notes.toString().isNotEmpty()) book.notes.toString() else "No thoughts available"){notes->
    notesText.value=notes



    }

    Row (modifier=Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){

        Spacer(modifier = Modifier.width(50.dp))
        TextButton(onClick = {isStartedReading.value=true}, enabled = book.startedReading==null,colors = ButtonDefaults.textButtonColors(contentColor = Color.Red, containerColor = Color.Transparent,)) {

            if (book.startedReading==null){

                if(!isStartedReading.value){
                    Text("Start Reading")
                }else{
                    Text("Started Reading", modifier = Modifier.alpha(0.6f), color = Color.Red.copy(alpha = 0.5f))
                }
            }else{
//                for date
                Text("Started on:${formatDate(book.startedReading!!)}")
            }


        }
        Spacer(modifier = Modifier.width(20.dp))
        TextButton(onClick = {isFinshedReading.value=true}, enabled = book.finishedReading==null, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red, containerColor = Color.Transparent,)) {

            if(book.finishedReading==null){

                if(!isFinshedReading.value){
                    Text("Mark as Read")
                }else{
                    Text("Finshed Reading")
                }
            }else{
                Text("End ${formatDate(book.finishedReading!!)}")
            }


        }








    }

    //        for the buttons and the thoughts in this screen
//        for the buttons and the thoughts in this screen

    val changedNotes = book.notes !=notesText.value

    val isFinishedTimpstamp = if(isFinshedReading.value) Timestamp.now()else book.finishedReading
    val isStartedtimestamp = if(isStartedReading.value) Timestamp.now() else book.startedReading
    val bookUpdate = changedNotes || isFinshedReading.value ||isFinshedReading.value

//                updating the firebase to change some fields

    val booktoUpdate = hashMapOf(
        "finished_reading_at" to isFinishedTimpstamp,
        "started_reading_at" to isStartedtimestamp,
        "notes" to notesText.value).toMap()

    Row(modifier = Modifier.padding(start = 50.dp), ) {
        RounderButton(label = "Update") {

            if(bookUpdate){
                //        for the buttons and the thoughts in this screen


                FirebaseFirestore.getInstance().collection("books").document(book.id!!).update(booktoUpdate).addOnCompleteListener{task->
                    Log.w("complete","done ${task.result}")
                }.addOnFailureListener{Log.w("error","error updating document")}

                showToast(context, msg = "book is updated")
                navController.navigate(ReaderScreens.HomeScreen.name)

            }


        }
        Spacer(modifier = Modifier.width(100.dp))


//        making alert dialog for deletion
        val opendialog = remember { mutableStateOf(false) }
            
        if (opendialog.value){

            ShowAlertDialog(message= stringResource(id = R.string.sure) + "/n" + stringResource(id = R.string.action),opendialog){

                FirebaseFirestore.getInstance().collection("books").document(book.id!!).delete().addOnCompleteListener{

                    if (it.isSuccessful){
                        opendialog.value=false
                    }

                    navController.navigate(ReaderScreens.HomeScreen.name)                }
            }

        }
        
        
        RounderButton(label = "delete") {

            opendialog.value=true

        }
    }

}

@Composable
fun ShowAlertDialog(message: String,
                    openDialog:MutableState<Boolean>,
                    onYesPressed:()->Unit) {

    if(openDialog.value){
        AlertDialog(onDismissRequest = {openDialog.value = false }, title = { Text("Delete Book") }, text = { Text(message) },

            dismissButton = {Button(onClick = { openDialog.value = false }) {
            Text("No")
        }},

            confirmButton = {
            Button(onClick = {
                openDialog.value = false  // Close dialog
                onYesPressed.invoke()     // Execute delete action
            }) {
                Text("Yes")
            }
        })
    }

}




fun showToast(context: android.content.Context, msg: String) {
    Toast.makeText(context,msg,Toast.LENGTH_LONG).show()

}


@Composable
fun SimpleForm(modifier: Modifier=Modifier,loading:Boolean=false,defaultvalues:String="Great Book",onSearch:(String)->Unit){

    Column {
        val TextFieldValues= rememberSaveable{
            mutableStateOf(defaultvalues)
        }

        val keyboardcontroller= LocalSoftwareKeyboardController.current

        val valid = remember(TextFieldValues.value) {TextFieldValues.value.trim().isNotEmpty() }


        InputField(modifier=Modifier
            .padding(3.dp)
            .height(140.dp)
            .padding(horizontal = 20.dp, vertical = 12.dp),valueState = TextFieldValues, lableID = "Enter yout thoughts" ,enabledd = true, oneAction = KeyboardActions{
            if(!valid)return@KeyboardActions
            onSearch(TextFieldValues.value.trim())
            keyboardcontroller?.hide()
        })





    }
}





@Composable
fun ShowBookUpdate(bookInfo: DataorException<List<Mbooks>, Boolean, Exception>,bookItemId: String) {

Row {
    Spacer(modifier = Modifier.width(43.dp))
    if(bookInfo.data!=null){
        Column (modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center){

            CardListItem(book= bookInfo.data!!.first(){ mbook->
                mbook.googleBooksID == bookItemId
            },onPressDetails={})
        }
    }

}

}

@Composable
fun CardListItem(book: Mbooks, onPressDetails: () -> Unit) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { onPressDetails() }
        ) {
            Image(
                painter = rememberAsyncImagePainter(book.photourl.toString()),
                contentDescription = "image",
                modifier = Modifier
                    .size(150.dp) // Equivalent to width(150.dp).height(150.dp)
                    .clip(RoundedCornerShape(100.dp))
            )
        }

        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = book.tittl.toString(),
                modifier = Modifier
                    .padding(4.dp)
                    .width(150.dp), // Keep the text width controlled
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Text(
                text = book.publishedData.toString(),
                modifier = Modifier
                    .padding(1.dp)
                    .width(150.dp), // Same width to maintain alignment
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Text(
                text = book.author.toString(),
                modifier = Modifier
                    .padding(0.dp)
                    .width(150.dp), // Same width to maintain alignment
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }




    }
}


