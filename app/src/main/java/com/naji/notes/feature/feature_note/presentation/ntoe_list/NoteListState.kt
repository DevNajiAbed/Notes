package com.naji.notes.feature.feature_note.presentation.ntoe_list

import com.naji.notes.feature.feature_note.domain.model.Note
import com.naji.notes.feature.feature_note.domain.util.NoteOrder
import com.naji.notes.feature.feature_note.domain.util.OrderType

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
