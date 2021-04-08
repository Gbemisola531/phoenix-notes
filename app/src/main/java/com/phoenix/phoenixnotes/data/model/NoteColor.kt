package com.phoenix.phoenixnotes.data.model

import androidx.compose.ui.graphics.Color

data class NoteColor(
    val id: String,
    val color: Color,
    var isSelected: Boolean
)
