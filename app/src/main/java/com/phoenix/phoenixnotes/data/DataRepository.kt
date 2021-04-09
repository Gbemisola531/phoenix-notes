package com.phoenix.phoenixnotes.data

import com.phoenix.phoenixnotes.data.fake.FakeDataSource
import com.phoenix.phoenixnotes.data.local.LocalDataSource
import com.phoenix.phoenixnotes.data.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val fakeDataSource: FakeDataSource,
    private val localDataSource: LocalDataSource
) : DataRepositorySource {

    override suspend fun getAllNotes(): Flow<List<Note>> = localDataSource.getAllNotes()

    override suspend fun saveNote(note: Note) {
        localDataSource.saveNote(note)
    }

    override suspend fun updateNote(note: Note) {
        localDataSource.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        localDataSource.deleteNote(note)
    }
}