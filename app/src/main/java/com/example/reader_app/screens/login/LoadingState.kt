package com.example.reader_app.screens.login

data class LoadingState(val status: Stauts,val message : String?= null){

    companion object{
        val IDEl = LoadingState(Stauts.IDEL)
       val FALIURE = LoadingState(Stauts.FALIURE)
        val SUCCESS= LoadingState(Stauts.SUCCESS)
        val LOADING = LoadingState(Stauts.LOADING)
    }

    enum class Stauts{
        IDEL,
        SUCCESS,FALIURE,LOADING

    }



}
