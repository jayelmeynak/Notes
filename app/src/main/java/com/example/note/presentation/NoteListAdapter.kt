package com.example.note.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.databinding.NoteLayoutBinding
import com.example.note.domain.Note

class NoteListAdapter(): ListAdapter<Note,NoteListAdapter.NoteViewHolder>(NoteDiffCallback()) {

    var onNoteClickListener: ((Note) -> Unit)? = null

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

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
        holder.itemView.setOnClickListener{
            onNoteClickListener?.invoke(note)
        }
    }

}
