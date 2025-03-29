package com.example.reader_app.screens.search

import android.util.Log
import androidx.annotation.BoolRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reader_app.data.DataorException
import com.example.reader_app.model.Book
import com.example.reader_app.model.Item
import com.example.reader_app.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    // MutableState to track the state of the books data
    var listofBooks: MutableState<DataorException<List<Item>, Boolean, Exception>> =
        mutableStateOf(DataorException(data = null, loading = true, e = null))

//    for progress indicator
//    var isLoading:Boolean by mutableStateOf(true)

//    initially kuch to hone chahiye data mai in liye init
    init {
        SearchBooks("android") // Default query to fetch initial data
    }

//    launch because so that it does not interact or get disturbet with others
    fun SearchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch

            // Set loading state
            listofBooks.value = DataorException(loading = true, data = null, e = null)

            try {
                // Fetch books from repository
                val result = repository.getBooks(query)
//                recompost hua na issi liye dubara data mai result.data kiya
                listofBooks.value = DataorException(
                    data = result.data,
                    loading = false,
                    e = null
                )
            } catch (e: Exception) {
                // Handle exception and update state
                listofBooks.value = DataorException(
                    data = null,
                    loading = false,
                    e = e
                )
                Log.e("BookSearchViewModel", "Error fetching books: ${e.message}")
            }
        }
    }
}


