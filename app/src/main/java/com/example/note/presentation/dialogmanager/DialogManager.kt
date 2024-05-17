package com.example.note.presentation.dialogmanager

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.note.R


object DialogManager {

    fun changeNoteColor(context: Context, listener: Listener) {
        var color: Int
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val customLayout = inflater.inflate(R.layout.note_color_customized, null)
        builder.setView(customLayout)

        val dialog = builder.create()
        dialog.show()
        val bCancel = customLayout.findViewById<Button>(R.id.bCancel)
        val bOk = customLayout.findViewById<Button>(R.id.bOk)
        val bRadioGroup = customLayout.findViewById<RadioGroup>(R.id.rgChangeColor)
        bCancel.setOnClickListener {
            dialog.dismiss()
        }

        bOk.setOnClickListener {
            when (val checkedRadioButtonId = bRadioGroup.checkedRadioButtonId) {
                -1 -> {
                    Toast.makeText(context, R.string.choose_color, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val selectedRadioButton =
                        customLayout.findViewById<RadioButton>(checkedRadioButtonId)
                    color = selectedRadioButton.textColors.defaultColor
                    listener.onClick(color)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    fun filterNoteColor(context: Context, listener: Listener) {
        var color: Int
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val customLayout = inflater.inflate(R.layout.note_filter, null)
        builder.setView(customLayout)

        val dialog = builder.create()
        dialog.show()
        val bCancel = customLayout.findViewById<Button>(R.id.bFilterCancel)
        val bOk = customLayout.findViewById<Button>(R.id.bFilterOk)
        val bRadioGroup = customLayout.findViewById<RadioGroup>(R.id.rgFilterColor)
        bCancel.setOnClickListener {
            dialog.dismiss()
        }

        bOk.setOnClickListener {
            when (val checkedRadioButtonId = bRadioGroup.checkedRadioButtonId) {
                -1 -> {
                    Toast.makeText(context, R.string.choose_color, Toast.LENGTH_SHORT).show()
                }

                else -> {

                    val selectedRadioButton =
                        customLayout.findViewById<RadioButton>(checkedRadioButtonId)
                    color = selectedRadioButton.textColors.defaultColor
                    listener.onClick(color)
                    dialog.dismiss()
                    listener.onClick(color)
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    fun deleteNote(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val customLayout = inflater.inflate(R.layout.dialog_delete_note, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.show()
        val bCancel = customLayout.findViewById<Button>(R.id.bDel)
        val bOk = customLayout.findViewById<Button>(R.id.bCancel)
        bOk.setOnClickListener{
            listener.onClick(0)
            dialog.dismiss()
        }
        bCancel.setOnClickListener{
            listener.onClick(1)
            dialog.dismiss()
        }
        dialog.show()
    }


    interface Listener {

        fun onClick(name: Int)
    }
}