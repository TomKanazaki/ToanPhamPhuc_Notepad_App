package com.example.toanphamphuc_notepad_app.old_packages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.toanphamphuc_notepad_app.viewModel_Data.NoteData

@Dao
//define interface to interact with the database
interface NoteDao {
    @Insert
    suspend fun insertNote(note: NoteData)

    @Delete
    suspend fun deleteNote(note: NoteData)

    @Update
    suspend fun updateNote(note: NoteData)

//    @Query("SELECT * FROM notes")
//    fun getAllNotes(): Flow<List<NoteData>>
//
//    @Query("SELECT * FROM notes WHERE id = :noteId")
//    suspend fun getNoteById(noteId: Int): NoteData?
}