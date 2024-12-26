package com.naji.notes.feature.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.naji.notes.feature.feature_note.presentation.ui.theme.BabyBlue
import com.naji.notes.feature.feature_note.presentation.ui.theme.LightGreen
import com.naji.notes.feature.feature_note.presentation.ui.theme.RedOrange
import com.naji.notes.feature.feature_note.presentation.ui.theme.RedPink
import com.naji.notes.feature.feature_note.presentation.ui.theme.Violet

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(
            RedOrange,
            LightGreen,
            Violet,
            BabyBlue,
            RedPink
        )
    }
}

class InvalidNoteException(message: String): Exception(message)
