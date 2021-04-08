package com.phoenix.phoenixnotes.data.fake

import com.phoenix.phoenixnotes.data.model.Note
import com.phoenix.phoenixnotes.ui.theme.Cyan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class FakeData @Inject constructor() : FakeDataSource {

    @ExperimentalUnsignedTypes
    override suspend fun getAllNotes(): Flow<List<Note>> {
        return flow {
            emit(((0..50).map {
                Note(
                    it.toString(),
                    "Title $it",
                    "test content $it",
                    Date().toString(),
                    Cyan.value.toLong()
                )
            }))
        }
    }
}