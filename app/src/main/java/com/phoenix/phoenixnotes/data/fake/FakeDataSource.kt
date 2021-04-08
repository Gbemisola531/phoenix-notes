package com.phoenix.phoenixnotes.data.fake

import com.phoenix.phoenixnotes.data.model.Note
import kotlinx.coroutines.flow.Flow

interface FakeDataSource {
    suspend fun getAllNotes(): Flow<List<Note>>
}