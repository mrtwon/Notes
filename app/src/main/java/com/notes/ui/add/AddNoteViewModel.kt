package com.notes.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDbo
import com.notes.di.DependencyManager
import com.notes.ui.edit.NoteEdit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AddNoteViewModel: ViewModel() {
    private val noteDatabase = DependencyManager.noteDatabase()
    private val _successful =  MutableLiveData<Boolean>()
    val successful: LiveData<Boolean> = _successful
    fun addNote(noteData: NoteData){
        viewModelScope.launch(Dispatchers.IO){
            noteDatabase.
            noteDao().
            insertAll(
                noteData.toNoteDbo()
            )

            _successful.postValue(true)
        }
    }
}
data class NoteData(val title: String, val content: String) {
    fun toNoteDbo(): NoteDbo {
        val currentTime = LocalDateTime.now()
        return NoteDbo(
            title = title,
            content = content,
            createdAt = currentTime,
            modifiedAt = currentTime
        )
    }
}