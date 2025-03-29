package com.example.reader_app.screens.details

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reader_app.components.ReaderAppBar
import com.example.reader_app.components.RounderButton
import com.example.reader_app.data.DataorException
import com.example.reader_app.model.Item
import com.example.reader_app.model.Mbooks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun BookDetail(navController: NavController,bookId:String,
               viewmodel: detailScreenViewmodel= hiltViewModel()
){

    Scaffold (topBar = { ReaderAppBar(title = "Reader_App", icon = Icons.AutoMirrored.Filled.ArrowBack,navController=navController, ShowProfile = false) {
        navController.popBackStack()
    } }){ innerpadding->

        Surface(modifier = Modifier.padding(innerpadding).fillMaxSize()) {

            Column(modifier = Modifier, verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {



//                ye viewmodel.launch hai
                val bookinfo= produceState<DataorException<Item,Boolean,Exception>>(initialValue =DataorException(data = null,true, e = null)){
                    value = viewmodel.getBookInfo(bookId)
                }.value

                if (bookinfo.loading == true) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else if (bookinfo.data != null ) {

                    ShowBookDetail(bookinfo,navController)




                } else {
                    // Handle the case where data is not available or title is null
                    Text("Book information not available.")
                }

            }
        }

    }
}

@Composable
fun ShowBookDetail(bookinfo: DataorException<Item, Boolean, Exception>, navController: NavController) {

    val bookData = bookinfo.data?.volumeInfo
    val googleBookId= bookinfo.data?.id
    Surface(modifier = Modifier.padding(17.dp).height(900.dp).width(400.dp), shadowElevation = 4.dp, color = Color(0xFF019898), shape = RoundedCornerShape(10.dp)) {


        Column (modifier = Modifier.fillMaxSize(),  horizontalAlignment = Alignment.CenterHorizontally){

        Card (modifier = Modifier.padding(34.dp), shape = CircleShape, ){

            if (bookData != null) {
                Image(painter = rememberAsyncImagePainter(model = bookData.imageLinks.toString()),contentDescription = "img", modifier = Modifier.height(90.dp).width(90.dp).padding(1.dp))


            }
        }

            if (bookData != null) {

                Text(
                    text = "Book Name : ${bookData.title}", // 🔹 Handle null title
                    overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium, fontSize = 23.sp
                )
                    Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Authors : ${bookData.authors}", // 🔹 Handle null title
                    overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium, fontSize = 23.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "categories : ${bookData.categories}", // 🔹 Handle null title
                    overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium, fontSize = 18.sp
                )
//                tp remove html marks from paragraph
                val cleanDris = HtmlCompat.fromHtml(bookData.description,HtmlCompat.FROM_HTML_MODE_LEGACY)


                val localDims = LocalContext.current.resources.displayMetrics
                Surface (modifier = Modifier.padding(10.dp).height(200.dp)){

                    LazyColumn(modifier = Modifier.padding(8.dp)) {

                        item {

                            Text(cleanDris.toString())
                        }
                    }
                }

               Row (modifier = Modifier.padding(top=6.dp), horizontalArrangement = Arrangement.SpaceAround){

                   RounderButton("Save",) {
//                       save this book to firestore database

                    val book=Mbooks(tittl = bookData.title, author = bookData.authors.toString(), description = bookData.description, categories = bookData.categories.toString(), notes = "", publishedData = bookData.publishedDate, photourl = bookData.imageLinks.thumbnail, pagecount = bookData.pageCount.toString(), rating = bookData.ratingsCount.toDouble(), googleBooksID = googleBookId, userId = FirebaseAuth.getInstance().currentUser?.uid.toString())

                       saveToFirebase(book,navController)

                   }

                   Spacer(modifier = Modifier.width(25.dp))

                   RounderButton("Cancel") {
                       navController.popBackStack()
                   }
               }


            }

        }
    }

}


fun saveToFirebase(book: Mbooks,navController: NavController) {
    val db= FirebaseFirestore.getInstance()
    val dbCollection =db.collection("books")

    if(book.toString().isNotEmpty()){
        dbCollection.add(book).addOnSuccessListener { documentRef->
//i updated the excluded id of titles in firestore with logined id
            val docId = documentRef.id
            dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String,Any>).addOnCompleteListener { task->

                if(task.isSuccessful){
                    navController.popBackStack()
                }
            }.addOnFailureListener{

                Log.w(TAG," saveto: in saving",it)
            }
        }
    }

}

