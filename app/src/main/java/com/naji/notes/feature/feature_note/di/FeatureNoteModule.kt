package com.naji.notes.feature.feature_note.di

import android.app.Application
import androidx.room.Room
import com.naji.notes.feature.feature_note.data.data_source.local.NoteDatabase
import com.naji.notes.feature.feature_note.data.data_source.local.dao.NoteDao
import com.naji.notes.feature.feature_note.data.repository.NoteRepositoryImpl
import com.naji.notes.feature.feature_note.domain.repository.NoteRepository
import com.naji.notes.feature.feature_note.domain.use_case.AddNoteUseCase
import com.naji.notes.feature.feature_note.domain.use_case.DeleteNoteUseCase
import com.naji.notes.feature.feature_note.domain.use_case.GetNoteUseCase
import com.naji.notes.feature.feature_note.domain.use_case.GetNotesUseCase
import com.naji.notes.feature.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeatureNoteModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository)
        )
    }
}