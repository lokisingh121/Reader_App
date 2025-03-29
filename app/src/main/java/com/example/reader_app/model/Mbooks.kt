package com.example.reader_app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Mbooks( var author:String? = null,
    var tittl:String?=null,
                   @Exclude var id:String?=null,
    var notes: String?=null,
//                   it should look likephoto_url in firebase
                   @get:PropertyName("book_photo_url")
                   @set:PropertyName("book_photo_url")
                    var photourl:String?=null,
                   var categories:String?=null,

                   @get:PropertyName("published_date")
                   @set:PropertyName("published_date")
                   var publishedData:String?=null,
                   var rating:Double?=null,
                   var description:String?=null,

                   var pagecount:String?=null,

                   @get:PropertyName("started_reading_at")
                   @set:PropertyName("started_reading_at")
                   var startedReading:Timestamp?=null,


                   @get:PropertyName("finished_reading_at")
                   @set:PropertyName("finished_reading_at")
                   var finishedReading:Timestamp?=null,


                   @get:PropertyName("user_id")
                   @set:PropertyName("user_id")
                   var userId:String?=null,


                   @get:PropertyName("google_books_id")
                   @set:PropertyName("google_books_id")
                   var googleBooksID:String?=null)
