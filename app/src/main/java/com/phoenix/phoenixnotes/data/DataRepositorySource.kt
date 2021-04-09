package com.phoenix.phoenixnotes.data

import com.phoenix.phoenixnotes.data.model.Note
import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {

    suspend fun getAllNotes(): Flow<List<Note>>

    suspend fun saveNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)
}