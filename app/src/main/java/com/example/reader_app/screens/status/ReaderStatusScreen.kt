package com.example.reader_app.screens.status

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reader_app.components.ReaderAppBar
import com.example.reader_app.model.Item
import com.example.reader_app.model.Mbooks
import com.example.reader_app.navigations.ReaderScreens
import com.example.reader_app.screens.home.HomeScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun ReaderStatusScreen(navController: NavController, ViewModel: HomeScreenViewModel= hiltViewModel()) {

    var books:List<Mbooks>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold (
        topBar = {
            ReaderAppBar(title = "Book Status", icon = Icons.AutoMirrored.Filled.ArrowBack, ShowProfile = false,navController=navController) {
                navController.popBackStack()
            }

        }

    ){innerpadding->
        Surface(modifier = Modifier.padding(innerpadding)) {

//            only to show books by this user each user has its own books


                books=if(!ViewModel.data.value.data.isNullOrEmpty()){
                    ViewModel.data.value.data!!.filter { mbooks->
                        (mbooks.userId == currentUser?.uid)
                    }
                }
            else{
             emptyList()
         }
            Column {
                Row {
                    Box(modifier = Modifier.size(50.dp).padding(2.dp)){
                        Icon(imageVector = Icons.Default.Person, contentDescription = "image", modifier = Modifier.padding(7.dp).size(42.dp))
                    }
                    if (currentUser != null) {
                        Text("Hi, ${currentUser.email.toString().split("@")[0]}", modifier = Modifier.padding(7.dp),fontSize = 18.sp)
                    }
                }

                Card(modifier = Modifier.fillMaxWidth().padding(4.dp), shape = CircleShape, ) {

                    val ReadingBooks=if(!ViewModel.data.value.data.isNullOrEmpty()){
                        books.filter { mbooks->
                            (mbooks.userId == currentUser?.uid) && (mbooks.finishedReading!=null)
                        }
                    }
                    else{
                        emptyList()
                    }


                    val readingbooks=books.filter {mbooks->
                        (mbooks.startedReading!=null) && (mbooks.finishedReading==null)
                    }


                    Column (modifier = Modifier.padding(15.dp), horizontalAlignment = Alignment.Start){

                        Text("Your Stats", fontWeight = FontWeight.SemiBold )
                        HorizontalDivider()
                        Text("your reading : ${readingbooks.size}")
                        Text("your read : ${ReadingBooks.size}")

                    }
                }

                if(ViewModel.data.value.loading==true){
                    LinearProgressIndicator()
                }else{
                    HorizontalDivider()
                    LazyColumn (modifier = Modifier.fillMaxWidth().fillMaxHeight(), contentPadding = PaddingValues(16.dp)){

//                        filter book finished ones
                        val readBooks:List<Mbooks> = if(!ViewModel.data.value.data.isNullOrEmpty()){
                            ViewModel.data.value.data!!.filter { mbooks->
                                (mbooks.userId==currentUser?.uid) && (mbooks.finishedReading!=null) }

                        }else{
                            emptyList()
                        }
                        items(items = readBooks){books->
                            BookRoww(books)

                        }

                    }
                }

            }

        }

    }
}




@Composable
fun BookRoww(book: Mbooks, ) {

    Card(modifier = Modifier.clickable {
//        when we click card it should go to detail screen
//        navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")

    }.fillMaxWidth().height(100.dp).padding(6.dp), shape = RectangleShape, elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {

        Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.Top) {

//            Text("hello")
            val imgUrl: String = book.photourl.toString()

            Log.d("BookRow", "Image URL: $imgUrl")

            Image(
                painter = rememberAsyncImagePainter(
                    model = imgUrl,
//                    placeholder = painterResource(id = R.drawable.readerimage), // Optional placeholder image
//                    error = painterResource(id = R.drawable.readerimage) // Optional error fallback
                ),
                contentDescription = "Book Image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(4.dp)) // Optional: make the image corners rounded
            )

            Column {

                Text(
                    text = book.tittl ?: "No Title Available", // 🔹 Handle null title
                    overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold
                )

                Text(
                    text = book.author.toString(), // 🔹 Handle null authors list
                    overflow = TextOverflow.Ellipsis,
                    fontSize = TextUnit.Unspecified
                )

                Text(
                    text = book.publishedData.toString(), // 🔹 Handle null date
                    overflow = TextOverflow.Ellipsis
                )
//                    Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
//                    Text(text = book.volumeInfo.authors.toString(), overflow = TextOverflow.Ellipsis, fontSize = TextUnit.Unspecified)
//                Text(text = book.volumeInfo.publishedDate, overflow = TextOverflow.Ellipsis)



            }
        }
    }

}