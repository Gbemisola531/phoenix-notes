package com.phoenix.phoenixnotes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.phoenixnotes.data.DataRepositorySource
import com.phoenix.phoenixnotes.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeViewState())
    val homeState: StateFlow<HomeViewState> get() = _homeState

    private val _getAllNotes = MutableStateFlow<List<Note>>(listOf())

    init {
        viewModelScope.launch {
            _getAllNotes.map {
                HomeViewState(it)
            }.collect {
                _homeState.value = it
            }
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            dataRepository.getAllNotes().collect {
                _getAllNotes.value = it.reversed()
            }
        }
    }
}

data class HomeViewState(
    val notes: List<Note> = listOf()
)