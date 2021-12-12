package com.notes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteDbo>

    @Query("SELECT * FROM notes")
    fun getAllLiveData(): LiveData<List<NoteDbo>>

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun noteById(id: Long): NoteDbo

    @Update
    fun updateNote(note: NoteDbo)

    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteNote(id: Long)

}