package com.example.reader_app.screens.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reader_app.data.DataorException
import com.example.reader_app.model.Mbooks
import com.example.reader_app.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository):ViewModel(){

    val data: MutableState<DataorException<List<Mbooks>,Boolean,Exception>> = mutableStateOf(DataorException(
        listOf(),true,Exception("")))

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {

        viewModelScope.launch {
            data.value.loading=true
            data.value=repository.getAllBooksFromFireBase()

            if(!data.value.data.isNullOrEmpty()) data.value.loading =false
        }

        Log.d(TAG,"getallbook: ${data.value.data}")
    }

}