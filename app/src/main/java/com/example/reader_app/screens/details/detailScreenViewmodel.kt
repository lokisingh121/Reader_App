package com.example.reader_app.screens.details

import androidx.lifecycle.ViewModel
import com.example.reader_app.data.DataorException
import com.example.reader_app.model.Item
import com.example.reader_app.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class detailScreenViewmodel @Inject constructor(private val repository: BookRepository):ViewModel() {

//ham viewmodel mai repository ko ek mutable state banate hai yha bhi aisa ki kre ge par dusre tarike se ye tarika detailscreen mai hai
//    suspend fun getBookInfo(bookID:String):DataorException<Item,Boolean,Exception>{
//
//        return repository.getBookInfo(bookID)
//    }


    suspend fun getBookInfo(bookId: String): DataorException<Item, Boolean, Exception> {
        return repository.getBookInfo(bookId)
    }
}