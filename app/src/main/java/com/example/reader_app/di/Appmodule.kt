package com.example.reader_app.di

import com.example.reader_app.network.BooksApi
import com.example.reader_app.repository.BookRepository
import com.example.reader_app.repository.FireRepository
import com.example.reader_app.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Appmodule {

    @Singleton
    @Provides
    fun provideFireBookRepository()=FireRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))

//     mota mota we need to register our repository and api here to inject



//    ye ham actual object bna rhe api kaa  yha par retro fit  ko bol rha ki retrofit bookapi pe jaao vha jaise likha hai vaisa kro
//    aab ham repository mai jaaye ge vha
    @Singleton
    @Provides
    fun ProvideBookApi():BooksApi{

        return Retrofit.Builder()
            .baseUrl(Constants.Base_Url).addConverterFactory(GsonConverterFactory.create())
            .build().create(BooksApi::class.java)

    }


//    we provide out repository here so that we can inject
    @Singleton
    @Provides
    fun provideBookRepository(api: BooksApi)=BookRepository(api)

}