package com.notes.ui.add

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.notes.R
import com.notes.databinding.FragmentNoteAddBinding
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.log
import com.notes.ui.edit.EditNoteViewModel

class FragmentAddNote:
    ViewBindingFragment<FragmentNoteAddBinding>(FragmentNoteAddBinding::inflate),
    View.OnClickListener {

    /* будет правильнее получать через ViewModelProvider :) */
    private val vm: AddNoteViewModel by lazy { ViewModelProvider(this)[AddNoteViewModel::class.java] }
    private lateinit var et_title: TextInputEditText
    private lateinit var et_content: TextInputEditText

    private var currentTitle: String = ""
    private var currentContent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            currentTitle = it.getString("title", "")
            currentContent = it.getString("content", "")
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteAddBinding,
        savedInstanceState: Bundle?
    ) {
        et_title = viewBinding.textFieldTitle
        et_content = viewBinding.textFieldContentNotes

        viewBinding.btnConfirm.setOnClickListener(this)
        vm.successful.observe(this, ::onSuccessful)
        listenerTextInput()

        if(currentTitle.isNotEmpty()) et_title.setText(currentTitle)
        if(currentContent.isNotEmpty()) et_content.setText(currentContent)
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

    private fun addNote(){
        vm.addNote(
            NoteData(currentTitle, currentContent)
        )
    }

    private fun onSuccessful(state: Boolean){
        if(state) parentFragmentManager.popBackStack()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("title", currentTitle)
        outState.putString("content", currentContent)
        super.onSaveInstanceState(outState)
    }

    override fun onClick(v: View?) {
        if(v?.id  == R.id.btn_confirm){
            addNote()
            parentFragmentManager.popBackStack()
        }
    }

    companion object{
        fun instance(): FragmentAddNote{
            return FragmentAddNote()
        }
    }


}