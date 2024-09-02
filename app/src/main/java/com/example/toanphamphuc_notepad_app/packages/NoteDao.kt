package com.example.toanphamphuc_notepad_app.packages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.toanphamphuc_notepad_app.NoteData
import kotlinx.coroutines.flow.Flow

@Dao
//define interface to interact with the database
interface NoteDao {
    @Insert
    suspend fun insertNote(note: NoteData)

    @Delete
    suspend fun deleteNote(note: NoteData)

    @Update
    suspend fun updateNote(note: NoteData)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteData>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteData?
}