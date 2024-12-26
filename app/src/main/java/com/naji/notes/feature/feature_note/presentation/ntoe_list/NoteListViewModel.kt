package com.naji.notes.feature.feature_note.presentation.ntoe_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naji.notes.feature.feature_note.domain.model.Note
import com.naji.notes.feature.feature_note.domain.use_case.NoteUseCases
import com.naji.notes.feature.feature_note.domain.util.NoteOrder
import com.naji.notes.feature.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val useCases: NoteUseCases
): ViewModel() {

    var state by mutableStateOf(NoteListState())
        private set

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.ChangeOrder -> {
                if(state.noteOrder::class == event.noteOrder::class &&
                    state.noteOrder.orderType == event.noteOrder.orderType) {
                    // So there is nothing changed in the order, the same order has been passed in.
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    useCases.deleteNoteUseCase(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    useCases.addNoteUseCase(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                state = state.copy(
                    isOrderSectionVisible = !state.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = viewModelScope.launch {
            useCases.getNotesUseCase(noteOrder)
                .onEach { notes ->
                    state = state.copy(
                        notes = notes,
                        noteOrder = noteOrder
                    )
                }.launchIn(viewModelScope)
        }
    }
}