package com.example.masterplan.ui.chores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.masterplan.R

class ChoresAdapter(private val onDelete: (Int) -> Unit) :
    ListAdapter<Chore, ChoresAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Chore>() {
            override fun areItemsTheSame(a: Chore, b: Chore) = a.id == b.id
            override fun areContentsTheSame(a: Chore, b: Chore) = a == b
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_chore, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chore = getItem(position)
        holder.text.text = chore.name
        holder.removeBtn.setOnClickListener { onDelete(chore.id) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.text_chore_item)
        val removeBtn: ImageButton = view.findViewById(R.id.btn_remove_chore)
    }
}
