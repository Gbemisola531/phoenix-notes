package com.phoenix.phoenixnotes.ui.createNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.phoenixnotes.data.model.Note
import com.phoenix.phoenixnotes.data.model.NoteColor
import com.phoenix.phoenixnotes.utils.ColorUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.util.*
import javax.inject.Inject

@ExperimentalUnsignedTypes
@HiltViewModel
class CreateNoteStateViewModel @Inject constructor() : ViewModel() {

    private val _createNoteState = MutableStateFlow(CreateNoteState())
    val createNoteState: StateFlow<CreateNoteState> get() = _createNoteState

    private val _titleState = MutableStateFlow("")
    private val _contentState = MutableStateFlow("")
    private val _addOptionState = MutableStateFlow(false)
    private val _showActions = MutableStateFlow(false)
    private val _noteColorsState = MutableStateFlow(ColorUtil.noteColors())
    private val _selectedColorState = MutableStateFlow(_noteColorsState.value[0])
    val selectedColorState get() = _selectedColorState

    init {
        viewModelScope.launch {
            combine(
                _titleState, _contentState,
                _addOptionState, _noteColorsState,
                _showActions
            ) { title, content, addOption, noteColors, showActions ->
                val isNoteValid = title.isNotEmpty() || content.isNotEmpty()
                CreateNoteState(title, content, isNoteValid, addOption, showActions, noteColors)
            }.collect {
                _createNoteState.value = it
            }

            _selectedColorState.collect { _selectedColorState.value = it }
        }
    }

    fun onTitleValueChange(newValue: String) {
        _titleState.value = newValue
    }

    fun onContentValueChange(newValue: String) {
        _contentState.value = newValue
    }

    fun onAddClicked() {
        _addOptionState.value = _addOptionState.value.not()
    }

    fun onOptionsClicked() {
        _showActions.value = _showActions.value.not()
    }

    fun updateSelectedColor(selectedColor: NoteColor) {
        _selectedColorState.value = selectedColor
    }

    fun getNote(): Note {
        val createNoteState = _createNoteState.value

        val format = ISODateTimeFormat.dateTime()
        val date = format.parseDateTime(DateTime.now().toString())

        return Note(
            UUID.randomUUID().toString(),
            createNoteState.title,
            createNoteState.content, date.toString(),
            _selectedColorState.value.color.value.toLong()
        )
    }
}

data class CreateNoteState @ExperimentalUnsignedTypes constructor(
    val title: String = "",
    val content: String = "",
    val isNoteValid: Boolean = false,
    val addOption: Boolean = false,
    val showActions: Boolean = false,
    val noteColors: List<NoteColor> = listOf()
)