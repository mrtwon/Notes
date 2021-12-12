package com.notes.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.LocalDateTimeConverter
import com.notes.data.NoteDbo
import com.notes.di.AppComponent
import com.notes.di.DaggerAppComponent
import com.notes.di.DaggerRootComponent
import com.notes.di.DependencyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class EditNoteViewModel: ViewModel() {
    private val noteDatabase = DependencyManager.noteDatabase()
    private val noteMutableLiveData = MutableLiveData<NoteEdit>()
    val noteLiveData: LiveData<NoteEdit> = noteMutableLiveData

    private val _successful =  MutableLiveData<Boolean>()
    val successful: LiveData<Boolean> = _successful



    fun changeNote(noteEdit: NoteEdit){
       println("note id = ${noteEdit.id}")
        noteEdit.modifiedAt = LocalDateTime.now()
        viewModelScope.launch(Dispatchers.IO){
            noteDatabase
                .noteDao()
                .updateNote(noteEdit.toNoteDbo())

            _successful.postValue(true)
        }
    }

    fun getNote(id: Long){
        viewModelScope.launch(Dispatchers.IO){
            noteMutableLiveData.postValue(
                fromNoteDbo(
                    noteDatabase
                        .noteDao()
                        .noteById(id)
                )
            )
        }
    }

    private fun fromNoteDbo(noteDbo: NoteDbo): NoteEdit{
        return NoteEdit(
            id = noteDbo.id, title = noteDbo.title,
            content = noteDbo.content, createAt = noteDbo.createdAt,
            modifiedAt =  noteDbo.modifiedAt
        )
    }
}
    data class NoteEdit(
                val id: Long, var title: String,
                var content: String, val createAt: LocalDateTime,
                var modifiedAt: LocalDateTime){
    // convert NoteEdit to NoteDbo
    fun toNoteDbo(): NoteDbo{
        return NoteDbo(
            id = id, title = title,
            content =  content, createdAt = createAt,
            modifiedAt =  modifiedAt
            )
        }
    }
