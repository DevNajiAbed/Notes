package com.naji.notes.feature.feature_note.domain.use_case

import com.naji.notes.feature.feature_note.domain.model.Note
import com.naji.notes.feature.feature_note.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(noteId: Int): Note? {
        return repository.getNoteById(noteId)
    }
}