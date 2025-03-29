package com.example.reader_app.model

data class Muser(val id: String,
    val userID: String,
    val avatarUrl:String,
    val quote:String,
    val profession:String,
    val displayName:String){

    fun toMap():MutableMap<String,Any>{

        return mutableMapOf("user_id" to this.userID,
                            "display_name " to this.displayName,
                            "quote" to this.quote,
                            "profession" to this.profession,
                            "avatar_url" to this.avatarUrl)

    }


}
