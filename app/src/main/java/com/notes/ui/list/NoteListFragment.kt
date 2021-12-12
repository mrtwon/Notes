package com.notes.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.databinding.FragmentNoteAddBinding
import com.notes.databinding.FragmentNoteEditingBinding
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.di.DependencyManager
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementation
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.add.FragmentAddNote
import com.notes.ui.details.NoteDetailsFragment
import com.notes.ui.edit.FragmentEditNote

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {
    /* будет правильнее получать через ViewModelProvider :) */
    private val viewModel by lazy { ViewModelProvider(this)[NoteListViewModel::class.java] }

    private val recyclerViewAdapter = RecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)
        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            viewModel.onCreateNoteClick()
        }

        viewModel.notes.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    recyclerViewAdapter.setItems(it)
                }
            }
        )
        viewModel.navigateToNoteCreation.observe(
            viewLifecycleOwner,
            {
                if(it == null) return@observe
                findImplementation<FragmentNavigator>()?.navigateTo(
                    FragmentAddNote.instance()
                )

                // что-бы при переходе обратно не срабатаывало сново
                viewModel.nullPropertyForCreateNoteClick()
            }
        )
    }

    private inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size

        fun setItems(
            items: List<NoteListItem>
        ) {
           this.items.clear()
           this.items.addAll(items)
           notifyDataSetChanged()
           /*for(i in 0 until this.items.size){
               notifyItemChanged(i)
           }*/
        }

        private inner class ViewHolder(
            private val binding: ListItemNoteBinding
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {

            fun bind(
                note: NoteListItem
            ) {
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content
                binding.layoutContent.setOnClickListener{
                    findImplementation<FragmentNavigator>()?.navigateTo(
                        //FragmentAddNote.instance()
                        FragmentEditNote.instance(note.id)
                    )
                }
                binding.btnDelete.setOnClickListener{
                    viewModel.deleteNote(note)
                }
            }

        }

    }

}