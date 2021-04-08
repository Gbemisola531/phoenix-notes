package com.phoenix.phoenixnotes.data.local

import com.phoenix.phoenixnotes.data.local.dao.NoteDao
import com.phoenix.phoenixnotes.data.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalData @Inject constructor(
    private val noteDao: NoteDao
) : LocalDataSource {

    override suspend fun saveNote(note: Note) = noteDao.saveNote(note)

    override suspend fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
}