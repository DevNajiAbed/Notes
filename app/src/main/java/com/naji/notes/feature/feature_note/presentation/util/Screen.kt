package com.naji.notes.feature.feature_note.presentation.util

sealed class Screen(val route: String) {
    data object NoteListScreen: Screen("note_list_screen")
    data object AddEditNoteScreen: Screen("add_edit_note_screen")
}