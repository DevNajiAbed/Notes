package com.naji.notes.feature.feature_note.presentation.ntoe_list

import com.naji.notes.feature.feature_note.domain.model.Note
import com.naji.notes.feature.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class ChangeOrder(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data object RestoreNote: NotesEvent()
    data object ToggleOrderSection: NotesEvent()
}