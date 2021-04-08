package com.phoenix.phoenixnotes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.phoenix.phoenixnotes.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun saveNote(note: Note)

    @Query("Select * FROM note")
    fun getAllNotes(): Flow<List<Note>>
}