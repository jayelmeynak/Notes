package com.example.note.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.note.MainViewModel.MainViewModel
import com.example.note.R
import com.example.note.databinding.NoteLayoutBinding
import com.example.note.fragments.HomeFragmentDirections
import com.example.note.model.Note

class NoteAdapter(): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private var noteList = emptyList<Note>()

    class NoteViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = NoteLayoutBinding.bind(view)

        fun bind(note: Note) = with(binding){
            tvNoteTitle.text = note.noteTitle
            tvNoteDesc.text = note.noteDesc
            tvNoteDate.text = note.noteEditTime
            noteLeftLine.setBackgroundColor(note.noteColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_layout, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.bind(note)
        holder.itemView.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(users: List<Note>) {
        this.noteList = users
        notifyDataSetChanged()
    }
}
