package com.phoenix.phoenixnotes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey
    val id: String,
    var title: String,
    var content: String,
    var dateCreated: String,
    var color: Long
)
