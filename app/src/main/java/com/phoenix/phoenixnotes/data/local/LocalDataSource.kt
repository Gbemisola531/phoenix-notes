package com.phoenix.phoenixnotes.data.local

import com.phoenix.phoenixnotes.data.model.Note
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun getAllNotes(): Flow<List<Note>>
}