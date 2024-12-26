package com.naji.notes.feature.feature_note.presentation.add_edit_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naji.notes.feature.feature_note.domain.model.InvalidNoteException
import com.naji.notes.feature.feature_note.domain.model.Note
import com.naji.notes.feature.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var noteTitle by mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title"
        )
    )
        private set

    var noteContent by mutableStateOf(
        NoteTextFieldState(
            "Enter some content"
        )
    )
        private set

    var noteColor by mutableIntStateOf(Note.noteColors.random().toArgb())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteUseCase(noteId)?.also { note ->
                        currentNoteId = note.id
                        noteTitle = noteTitle.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        noteContent = noteContent.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        noteColor = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnterTitle -> {
                noteTitle = noteTitle.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitle = noteTitle.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnterContent -> {
                noteContent = noteContent.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                noteContent = noteContent.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                noteColor = event.color
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                title = noteTitle.text,
                                content = noteContent.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(UiEvent.ShowSnackBar(e.message ?: "Couldn't save note"))
                    }
                }
            }
        }
    }

    /**
     * This class to describe events that we want it to happen once, not again when
     * a configuration changes, such as showing a SnackBar, we don't want to show it
     * again when for example the screen rotated.
     * */
    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        data object SaveNote : UiEvent()
    }
}