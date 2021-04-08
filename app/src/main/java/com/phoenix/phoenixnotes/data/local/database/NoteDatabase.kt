package com.phoenix.phoenixnotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phoenix.phoenixnotes.data.local.dao.NoteDao
import com.phoenix.phoenixnotes.data.model.Note

@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}