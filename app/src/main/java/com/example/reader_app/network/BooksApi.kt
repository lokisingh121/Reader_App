package com.example.reader_app.network

import com.example.reader_app.model.Book
import com.example.reader_app.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


//at network we make api so that retrofit can go an take out data and its a interface class

//aab ham isregister kre ke  taaki har jagha isko inject kr ske and retrofit ko bhi btye ge ki ye api hai ye dono kaam ek hi sath hota hai di->app module mai

// aab repository mai inject kre ge and modify kre ge data ko thoda ,yaani aab mera sara maal thoda alag dikhta hai yaani aab isko phir se register krna pde gaa di->app module mai
//taaki mera modified data aab use ho

//ultimately mera final  data aab modified hai bookrepository naam se jo ki  dataorexception jaisa dikhta hai

//aab view model banye ge search screen ka

//aab view model mai ham apne data ko mutable state mai convert kre ge kyunki data wil not always be same

@Singleton
interface BooksApi {

//    notice in these function expexting a string so all the other funtion using this need to geta a parameter of stringg

//    this will give entire book list of  query q
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query:String):Book

//    this will give specific book details of based on book id
    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId")bookID:String):Item

}