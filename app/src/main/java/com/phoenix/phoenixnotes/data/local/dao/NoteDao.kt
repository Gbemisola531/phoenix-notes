package com.phoenix.phoenixnotes.data.local.dao

import androidx.room.*
import com.phoenix.phoenixnotes.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun saveNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("Select * FROM note")
    fun getAllNotes(): Flow<List<Note>>

}