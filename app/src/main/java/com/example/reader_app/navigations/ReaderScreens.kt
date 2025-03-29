package com.example.reader_app.navigations

enum class ReaderScreens {

    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReaderHomeScreen,
    SearchScreen,
    DetailScreen,
    ReaderStatsScreen,
    UpdateScreen,
    HomeScreen,
    StatusScreen;

    companion object {

        fun fromRoute(route:String):ReaderScreens = when(route.substringBefore("/")){

            SplashScreen.name ->SplashScreen
            LoginScreen.name ->LoginScreen
            CreateAccountScreen.name ->CreateAccountScreen
            ReaderStatsScreen.name ->ReaderStatsScreen
            DetailScreen.name -> DetailScreen
            UpdateScreen.name -> UpdateScreen
            SearchScreen.name -> SearchScreen
            ReaderHomeScreen.name -> ReaderHomeScreen
            HomeScreen.name -> HomeScreen

            else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
        }
    }

}