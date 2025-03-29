package com.example.reader_app.repository


import com.example.reader_app.data.DataorException
import com.example.reader_app.model.Mbooks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//query inject kr rhe haii matlab firebase ki query inject kr rhe hai vha se hi data le rhe hai naa
class FireRepository @Inject constructor(private val queryBook: Query) {

    suspend fun getAllBooksFromFireBase():DataorException<List<Mbooks>,Boolean,Exception>{

        val dataoOrException = DataorException<List<Mbooks>,Boolean,Exception>()

        try {
                dataoOrException.loading=true

//            getting data from firebase firestore
//            we are maping objecs to mbook objects
            dataoOrException.data=queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject((Mbooks::class.java))!!
            }
            if(!dataoOrException.data.isNullOrEmpty()){
                dataoOrException.loading=false
            }
        }catch (exception:FirebaseFirestoreException){
            dataoOrException.e=exception
        }
        return dataoOrException
    }
}