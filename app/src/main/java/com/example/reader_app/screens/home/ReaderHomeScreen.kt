package com.example.reader_app.screens.home

import android.nfc.Tag
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.reader_app.components.FABContent
import com.example.reader_app.components.ListCard
import com.example.reader_app.components.ReaderAppBar
import com.example.reader_app.components.TitleSection
import com.example.reader_app.model.Mbooks
import com.example.reader_app.navigations.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderHomeScreen(navController: NavController,viewModel: HomeScreenViewModel= hiltViewModel()){

Scaffold (topBar = {
    ReaderAppBar(title = "Reader App", navController = (navController)){}

}, floatingActionButton = {

    FABContent(){
//        what happen when i click floating button
        navController.navigate(ReaderScreens.SearchScreen.name)
    }

}){innerpadding->

    Surface(modifier = Modifier.padding(innerpadding).fillMaxSize()) {

//home content
        HomeContent(navController,viewModel)

    }
}

}


@Composable
fun HomeContent(navController: NavController,viewModel: HomeScreenViewModel){

//    this is the book i added before but now in fierstore there is my saved data
//    so i am getting data from firestore and pass here
//    val listofbooks= listOf(Mbooks(" ramesh tiwari","the Escape","ksdfj","hello"),Mbooks(" ramesh tiwari","the Escape","ksdfj","hello"),Mbooks(" ramesh tiwari","the Escape","ksdfj","hello"))

    var listofbooks= emptyList<Mbooks>()
    val currentUser=FirebaseAuth.getInstance().currentUser

    if(!viewModel.data.value.data.isNullOrEmpty()){
        if (currentUser != null) {
            listofbooks = viewModel.data.value.data?.toList()!!.filter { mbook ->

                mbook.userId==currentUser.uid
            }
        }

        Log.d("Books","homecontent:${listofbooks.toString()}")
    }


    val currentuser = if(!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){

        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    }else{
        "not available"
    }
    Column(modifier = Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.Top) {

        Row(modifier = Modifier.align(  Alignment.Start)) {
            TitleSection(label = "Your Reading \n" + "activity now...")
            Spacer(modifier = Modifier.fillMaxWidth(0.8f))
            Column() {
                Icon(imageVector = Icons.Default.Person, contentDescription = "profile", modifier = Modifier.size(40.dp).clickable {
                    navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                })

                Text(text = currentuser!!, modifier = Modifier.padding(1.dp), fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Clip,)

            }

        }

        Spacer(modifier = Modifier.height(20.dp))
        ReadingRightNowArea(books = listofbooks ,navController=navController)
        Spacer(modifier = Modifier.height(20.dp))

        TitleSection(label = "Reading List...", modifier = Modifier.padding(4.dp))
        Spacer(modifier = Modifier.height(10.dp))

        BoookAreaList(listofbooks= listofbooks,navController=navController)

    }





}

@Composable
fun BoookAreaList(listofbooks: List<Mbooks>, navController: NavController) {

    val addedbooks = listofbooks.filter { mbooks ->
        mbooks.startedReading ==null && mbooks.finishedReading==null
    }
    
    HorizontalScrollableComponenet(addedbooks){

        //                on press the card of rading list goes to detail screen
        navController.navigate(ReaderScreens.UpdateScreen.name+"/$it")
    }





}

@Composable
fun HorizontalScrollableComponenet(listofbooks: List<Mbooks>, viewModel: HomeScreenViewModel= hiltViewModel(), onCardPress:(String)->Unit) {

    val horzontalscrollableview= rememberScrollState()

    Row (modifier = Modifier.fillMaxWidth().height(280.dp).horizontalScroll(horzontalscrollableview), ){

        if(viewModel.data.value.loading == true){
            LinearProgressIndicator()
        }else{

            if(listofbooks.isNullOrEmpty()){

                Surface(modifier = Modifier.padding(23.dp)) {
                    Text("No book found. Add a book", style = TextStyle(color = Color.Red.copy(alpha = 0.4f), fontWeight = FontWeight.SemiBold, fontSize = 14.sp))
                }
            }else{


                for (books in listofbooks){
                    ListCard(books) {


                        onCardPress(books.googleBooksID.toString())
                    }
                }
            }


        }




    }

}


@Composable
fun ReadingRightNowArea(books:List<Mbooks>, navController: NavController){

    val horzontalscrollableview= rememberScrollState()

//    fil;ter book
    val readingnowlist=books.filter { mbooks ->
        mbooks.startedReading !=null && mbooks.finishedReading==null
    }

//    Row (modifier = Modifier.fillMaxWidth().height(280.dp).horizontalScroll(horzontalscrollableview), ){
//
//        for (books in books){
//            ListCard(books) {
//
////                onCardPress(books.googleBooksID.toString())
//
//            }
//        }
//
//
//    }

    HorizontalScrollableComponenet(readingnowlist) {

        navController.navigate(ReaderScreens.UpdateScreen.name+"/$it")
    }



}