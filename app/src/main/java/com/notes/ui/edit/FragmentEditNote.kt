package com.notes.ui.edit

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.android.material.textfield.TextInputEditText
import com.notes.R
import com.notes.databinding.FragmentNoteEditingBinding
import com.notes.ui._base.ViewBindingFragment
import kotlin.properties.Delegates

class FragmentEditNote:
    ViewBindingFragment<FragmentNoteEditingBinding>(FragmentNoteEditingBinding::inflate),
    View.OnClickListener{

    /* будет правильнее получать через ViewModelProvider :) */
    private val vm: EditNoteViewModel by lazy { ViewModelProvider(this)[EditNoteViewModel::class.java] }

    private var idNotes: Long? = null
    private var currentNoteEdit: NoteEdit? = null

    private lateinit var et_title: TextInputEditText
    private lateinit var et_content: TextInputEditText

    private var currentTitle: String = ""
    private var currentContent: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            currentTitle = it.getString("title", "")
            currentContent = it.getString("content", "")
        }
        arguments?.let {
            idNotes = it.getLong("idNotes")
        }
        super.onCreate(savedInstanceState)
    }


    override fun onViewBindingCreated(
        viewBinding: FragmentNoteEditingBinding,
        savedInstanceState: Bundle?
    ) {
        et_title = viewBinding.textFieldTitle
        et_content = viewBinding.textFieldContentNotes
        et_title.setText(currentTitle)
        et_content.setText(currentContent)

        vm.successful.observe(this, ::onSuccessful)
        viewBinding.btnConfirm.setOnClickListener(this)
        listenerTextInput()
        when(vm.noteLiveData.value){
            null -> {
                vm.noteLiveData.observe(this, ::updateField)
                vm.noteLiveData.observe(this, ::initNote)
                idNotes?.let { id -> vm.getNote(id) }
            }
            else -> {
                currentNoteEdit = vm.noteLiveData.value
            }
        }
        super.onViewBindingCreated(viewBinding, savedInstanceState)
    }

    private fun listenerTextInput(){
        et_title.addTextChangedListener {
            currentTitle = it.toString()
        }
        et_content.addTextChangedListener {
            currentContent = it.toString()
        }
    }

    private fun updateField(noteEdit: NoteEdit){
        et_title.text?.clear()
        et_content.text?.clear()
        et_title.text?.append(noteEdit.title)
        et_content.text?.append(noteEdit.content)
    }

    private fun initNote(noteEdit: NoteEdit){
        currentNoteEdit = noteEdit
    }

    private fun onSuccessful(state: Boolean){
        if(state) parentFragmentManager.popBackStack()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("title", currentTitle)
        outState.putString("content", currentContent)
        super.onSaveInstanceState(outState)
    }



    companion object{
        fun instance(idNotes: Long): FragmentEditNote{
            return FragmentEditNote().apply {
                arguments = Bundle().apply {
                    this.putLong("idNotes", idNotes)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.btn_confirm){
            currentNoteEdit?.let {
                it.title = currentTitle
                it.content = currentContent
                vm.changeNote(it)
            }
        }
    }
}