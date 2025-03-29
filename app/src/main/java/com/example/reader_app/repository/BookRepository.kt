package com.example.reader_app.repository

import com.example.reader_app.data.DataorException
import com.example.reader_app.model.Item
import com.example.reader_app.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

val dataorexception =DataorException<List<Item>,Boolean,Exception>()

    val bookInfodataorexception =DataorException<Item,Boolean,Exception>()



    suspend fun getBooks(SearchQuery:String): DataorException<List<Item>,Boolean,Exception> {


        try {
            dataorexception.loading =true

            dataorexception.data=api.getAllBooks(SearchQuery).items
            if (dataorexception.data!!.isNotEmpty()) dataorexception.loading=false





        }catch (e:Exception){
            dataorexception.e=e
            dataorexception.loading = false
        }


     return dataorexception

    }

    suspend fun getBookInfo(bookID:String):DataorException<Item,Boolean,Exception>{
        bookInfodataorexception.loading = true // Start loading

        try {
            val response = api.getBookInfo(bookID) // Fetch data from API
            bookInfodataorexception.data = response // Assign fetched data

            bookInfodataorexception.loading = false // Stop loading
        } catch (e: Exception) {
            bookInfodataorexception.e = e
            bookInfodataorexception.loading = false // Stop loading even on error
        }

        return bookInfodataorexception
    }

//    suspend fun getBookInfo(bookId: String): DataorException<Item, Boolean, Exception> {
//        val bookInfo: Item?
//        try {
//            bookInfo = api.getBookInfo(bookId)
//        } catch (e: Exception) {
//            return DataorException(loading = false, e = e)
//        }
//
//        return DataorException(data = bookInfo, loading = false)
//    }


}