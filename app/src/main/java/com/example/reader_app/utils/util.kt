package com.example.reader_app.utils

import com.google.firebase.Timestamp
import java.text.DateFormat

fun formatDate(timestamp: Timestamp):String{
    val date = DateFormat.getInstance().format(timestamp.toDate()).toString().split(" ")[0]
    return date
}