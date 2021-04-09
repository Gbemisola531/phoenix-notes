package com.phoenix.phoenixnotes.utils

import com.phoenix.phoenixnotes.data.model.NoteColor
import com.phoenix.phoenixnotes.ui.theme.*

object ColorUtil {

    @ExperimentalUnsignedTypes
    fun noteColors(): List<NoteColor> {
        return mutableListOf<NoteColor>().apply {
            add(NoteColor(Black300))
            add(NoteColor(LightBlue))
            add(NoteColor(Cyan))
            add(NoteColor(Teal))
            add(NoteColor(Green))
            add(NoteColor(Yellow))
        }
    }
}