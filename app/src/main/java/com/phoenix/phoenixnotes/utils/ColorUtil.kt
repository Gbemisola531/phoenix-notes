package com.phoenix.phoenixnotes.utils

import com.phoenix.phoenixnotes.data.model.NoteColor
import com.phoenix.phoenixnotes.ui.theme.*
import java.util.*

object ColorUtil {

    @ExperimentalUnsignedTypes
    fun noteColors(): List<NoteColor> {
        return mutableListOf<NoteColor>().apply {
            add(NoteColor(UUID.randomUUID().toString(), Black300, true))
            add(NoteColor(UUID.randomUUID().toString(), LightBlue, false))
            add(NoteColor(UUID.randomUUID().toString(), Cyan, false))
            add(NoteColor(UUID.randomUUID().toString(), Teal, false))
            add(NoteColor(UUID.randomUUID().toString(), Green, false))
            add(NoteColor(UUID.randomUUID().toString(), Yellow, false))
        }
    }
}