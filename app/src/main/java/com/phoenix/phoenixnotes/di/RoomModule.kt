package com.phoenix.phoenixnotes.di

import android.content.Context
import androidx.room.Room
import com.phoenix.phoenixnotes.data.local.utils.DatabaseConstants.NOTE_DATABASE_NAME
import com.phoenix.phoenixnotes.data.local.dao.NoteDao
import com.phoenix.phoenixnotes.data.local.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(context, NoteDatabase::class.java, NOTE_DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun providesNoteDao(noteDatabase: NoteDatabase): NoteDao = noteDatabase.noteDao()
}