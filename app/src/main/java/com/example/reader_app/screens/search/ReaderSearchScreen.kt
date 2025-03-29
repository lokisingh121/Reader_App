package com.example.reader_app.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.reader_app.components.InputField
import com.example.reader_app.components.ReaderAppBar
import com.example.reader_app.model.Mbooks
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reader_app.R
import com.example.reader_app.model.Item
import com.example.reader_app.navigations.ReaderScreens


@Composable
fun ReaderSearchScreen(navController: NavController,viewModel: BookSearchViewModel= hiltViewModel()) {

    Scaffold (topBar = { ReaderAppBar("Search Books", icon = Icons.AutoMirrored.Filled.ArrowBack,navController=navController, ShowProfile = false){
        navController.navigate(ReaderScreens.HomeScreen.name)
//        navController.popBackStack()



    } }){ innerPaading->

        Surface(modifier = Modifier.padding(innerPaading)) {
val listofbooks=viewModel.listofBooks.value.data

//            Log.d("ReaderSearchScreen", "Books list: $listofbooks")
            Column {
                SearchForm(modifier = Modifier.fillMaxWidth().padding(16.dp),viewModel=viewModel, hint = "",){query->
                    viewModel.SearchBooks(query)


                }

                    Spacer(modifier = Modifier.height(20.dp))
                    BookList(listofbooks,navController,viewModel)




            }


        }




    }

}

@Composable
fun BookList(listofbooks: List<Item>?, navController: NavController, viewModel: BookSearchViewModel= hiltViewModel()) {

    if (viewModel.listofBooks.value.loading == true){
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }else if (viewModel.listofBooks.value.e != null) {
        Text(text = "Error loading books: ${viewModel.listofBooks.value.e?.message}", color = Color.Red) // 🔹 Show error message
    }else{

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(items = listofbooks ?: emptyList()){ book->
                BookRow(book,navController)

            }


        }
    }




}



@Composable
fun BookRow(book: Item, navController: NavController) {

    Card(modifier = Modifier.clickable {
//        when we click card it should go to detail screen
        navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")

    }.fillMaxWidth().height(100.dp).padding(6.dp), shape = RectangleShape, elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {

        Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.Top) {

//            Text("hello")
            val imgUrl: String = book.volumeInfo.imageLinks.smallThumbnail ?: ""

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
                    text = book.volumeInfo.title ?: "No Title Available", // 🔹 Handle null title
                    overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold
                )

                Text(
                    text = book.volumeInfo.authors.joinToString(", ") ?: "Unknown Author", // 🔹 Handle null authors list
                    overflow = TextOverflow.Ellipsis,
                    fontSize = TextUnit.Unspecified
                )

                Text(
                    text = book.volumeInfo.publishedDate ?: "No Date Available", // 🔹 Handle null date
                    overflow = TextOverflow.Ellipsis
                )
//                    Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
//                    Text(text = book.volumeInfo.authors.toString(), overflow = TextOverflow.Ellipsis, fontSize = TextUnit.Unspecified)
//                Text(text = book.volumeInfo.publishedDate, overflow = TextOverflow.Ellipsis)



            }
        }
    }

}

@Composable
fun SearchForm(modifier: Modifier=Modifier,
               loading:Boolean=false,
               hint:String,
    viewModel: BookSearchViewModel,
               onSearch:  (String)->Unit){

    Column {

        val searchQuearyState = rememberSaveable() { mutableStateOf("") }

        val keyboardOption= LocalSoftwareKeyboardController.current

        val valid = remember(searchQuearyState.value){searchQuearyState.value.trim().isNotEmpty()}

        InputField(valueState = searchQuearyState, lableID = "Search", enabledd = true, oneAction = KeyboardActions({
            if (!valid){
                return@KeyboardActions
            }else{
//                here i have what i hace searched
                onSearch(searchQuearyState.value.trim())
                searchQuearyState.value=""
            }
        } ))

    }
}