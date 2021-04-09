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

    private val _noteState = MutableStateFlow(NoteState())
    val noteState: StateFlow<NoteState> get() = _noteState

    private val _colorState = MutableStateFlow(ColorState())
    val colorState: StateFlow<ColorState> get() = _colorState

    private val _idState = MutableStateFlow(UUID.randomUUID().toString())
    private val _titleState = MutableStateFlow("")
    private val _contentState = MutableStateFlow("")

    private val _addOptionState = MutableStateFlow(false)
    private val _showActionsState = MutableStateFlow(false)

    private val _selectedColorState = MutableStateFlow(_colorState.value.selectedColor)

    init {
        viewModelScope.launch {
            combine(_addOptionState, _showActionsState) { addOption, showAction ->
                CreateNoteState(addOption, showAction)
            }.collect {
                _createNoteState.value = it
            }
        }

        viewModelScope.launch {
            _selectedColorState.collect {
                _colorState.value = ColorState(it)
            }
        }

        viewModelScope.launch {
            combine(_idState, _titleState, _contentState) { id, title, content ->
                val isNoteValid = title.isNotEmpty() || content.isNotEmpty()
                NoteState(id, title, content, isNoteValid)
            }.collect {
                _noteState.value = it
            }
        }
    }

    fun onIdValueChange(newId: String) {
        _idState.value = newId
    }

    fun onTitleValueChange(newValue: String) {
        _titleState.value = newValue
    }

    fun onContentValueChange(newValue: String) {
        _contentState.value = newValue
    }

    fun onAddClicked() {
        if (_showActionsState.value)
            _showActionsState.value = false

        _addOptionState.value = _addOptionState.value.not()
    }

    fun onOptionsClicked() {
        if (_addOptionState.value)
            _addOptionState.value = false

        _showActionsState.value = _showActionsState.value.not()
    }

    fun updateSelectedColor(selectedColor: NoteColor) {
        _selectedColorState.value = selectedColor
    }

    fun getNote(): Note {
        val noteState = _noteState.value

        val format = ISODateTimeFormat.dateTime()
        val date = format.parseDateTime(DateTime.now().toString())

        return Note(
            _idState.value,
            noteState.title,
            noteState.content, date.toString(),
            _selectedColorState.value.color.value.toLong()
        )
    }
}

data class CreateNoteState @ExperimentalUnsignedTypes constructor(
    val addOption: Boolean = false,
    val showActions: Boolean = false,
)

data class NoteState(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val isNoteValid: Boolean = false
)

@ExperimentalUnsignedTypes
data class ColorState(
    val selectedColor: NoteColor = ColorUtil.noteColors()[0],
    val noteColors: List<NoteColor> = ColorUtil.noteColors()
)