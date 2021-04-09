package com.phoenix.phoenixnotes.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.phoenix.phoenixnotes.utils.ColorUtil
import java.io.Serializable

@Entity
data class Note constructor(
    @PrimaryKey
    val id: String,
    var title: String,
    var content: String,
    var dateCreated: String,
    var color: Long
) : Serializable{
    @Ignore
    var isFromSpeech: Boolean = false
}
