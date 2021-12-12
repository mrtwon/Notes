package com.notes.ui.list

import android.util.Log
import androidx.lifecycle.*
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import com.notes.di.DependencyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteListViewModel : ViewModel() {
    /* здесь я бы предпочёл Hilt, можно создать ViewModel с inject конструкторов.
     оставлю как есть.
     */
    private val noteDatabase = DependencyManager.noteDatabase()


    // что бы получать актуальные данные - добавил LiveData от Room
    private val _notesLiveData: LiveData<List<NoteDbo>> = noteDatabase.noteDao().getAllLiveData()
    // мапинг данных что бы отдать их фрагменту
    val notes: LiveData<List<NoteListItem>> = Transformations.map(_notesLiveData){
        convertToNoteListItem(it)
    }

    private val _navigateToNoteCreation = MutableLiveData<Unit?>()
    val navigateToNoteCreation: LiveData<Unit?> = _navigateToNoteCreation


    fun onCreateNoteClick() {
        _navigateToNoteCreation.postValue(Unit)
    }

    // что-бы при переходе обратно не срабатаывало сново
    fun nullPropertyForCreateNoteClick(){
        _navigateToNoteCreation.postValue(null)
    }

    fun deleteNote(noteItem: NoteListItem){
        viewModelScope.launch(Dispatchers.IO){
            noteDatabase
                .noteDao()
                .deleteNote(
                    noteItem.id
                )
        }
    }

    private fun convertToNoteListItem(list: List<NoteDbo>): List<NoteListItem>{
        val resultList = arrayListOf<NoteListItem>()
        for(item in list){
            resultList.add(
                NoteListItem(id = item.id, title = item.title, content = item.content)
            )
        }
        return resultList
    }

}

data class NoteListItem(
    val id: Long,
    val title: String,
    val content: String,
)