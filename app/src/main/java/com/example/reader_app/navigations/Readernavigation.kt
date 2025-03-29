package com.example.reader_app.navigations

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reader_app.screens.ReaderSplashScreen
import com.example.reader_app.screens.details.BookDetail
import com.example.reader_app.screens.home.HomeScreenViewModel
import com.example.reader_app.screens.home.ReaderHomeScreen
import com.example.reader_app.screens.login.ReaderLoginScreen
import com.example.reader_app.screens.search.BookSearchViewModel
import com.example.reader_app.screens.search.ReaderSearchScreen
import com.example.reader_app.screens.status.ReaderStatusScreen
import com.example.reader_app.screens.update.ReaderUpdateScreen

@Composable
fun Readernavigation (){

    val navController = rememberNavController()
   NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){

       composable(ReaderScreens.SplashScreen.name){
           ReaderSplashScreen(navController = navController)
       }


//       Why Pass ViewModel from Navigation?
//       If you create it inside ReaderHomeScreen, a new instance might be created every time the function recomposes, leading to unnecessary reinitializations.
       composable(ReaderScreens.HomeScreen.name){
           val homeViewModel= hiltViewModel<HomeScreenViewModel>()
        ReaderHomeScreen(navController = navController, viewModel=homeViewModel)
       }



        val detailNames=ReaderScreens.DetailScreen.name
       composable("$detailNames/{bookId}", arguments = listOf(navArgument("bookId"){
           type= NavType.StringType
       })){ backstackentry->
           backstackentry.arguments?.getString("bookId").let {
               BookDetail(navController = navController, bookId = it.toString())
           }

       }
//       composable(ReaderScreens.DetailScreen.name){
//           BookDetail(
//               navController = navController,
//               bookId = ""
//           )
//       }



       composable(ReaderScreens.SearchScreen.name){
//           we need to do this for ui i think
           val searchViewModel = hiltViewModel<BookSearchViewModel>()

        ReaderSearchScreen(navController = navController, viewModel=searchViewModel)
       }



       val updateName=ReaderScreens.UpdateScreen.name
      composable("$updateName/{bookItemId}", arguments = listOf(navArgument("bookItemId"){type=NavType.StringType})){navBackStackEntry ->
          navBackStackEntry.arguments?.getString("bookItemId").let {
              if (it != null) {
                  ReaderUpdateScreen(navController = navController,bookItemId = it)
              }

          }

      }



       composable(ReaderScreens.ReaderStatsScreen.name){
           val homeViewModel = hiltViewModel<HomeScreenViewModel>()
           ReaderStatusScreen(navController = navController,ViewModel=homeViewModel)
       }

       composable(ReaderScreens.LoginScreen.name){
           ReaderLoginScreen(navController = navController)
       }




    }




}