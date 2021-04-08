package com.phoenix.phoenixnotes.ui.createNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.phoenixnotes.data.DataRepositorySource
import com.phoenix.phoenixnotes.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val dataRepositorySource: DataRepositorySource
) : ViewModel() {


    fun saveNote(note: Note) {
        viewModelScope.launch {
            dataRepositorySource.saveNote(note)
        }
    }
}
