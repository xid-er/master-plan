package com.example.masterplan.ui.calories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.masterplan.R

class CalorieEntriesAdapter(private val onDelete: (Int) -> Unit) :
    ListAdapter<CalorieEntry, CalorieEntriesAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CalorieEntry>() {
            override fun areItemsTheSame(a: CalorieEntry, b: CalorieEntry) = a.id == b.id
            override fun areContentsTheSame(a: CalorieEntry, b: CalorieEntry) = a == b
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_calorie_entry, parent, false)
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = getItem(position)
        holder.name.text = entry.name
        
        if (entry.type == EntryType.MEAL) {
            holder.calories.text = "+${entry.calories} kcal"
            holder.calories.setTextColor(0xFF4CAF50.toInt()) // Green
            holder.icon.setImageResource(android.R.drawable.ic_menu_add) 
        } else {
            holder.calories.text = "-${entry.calories} kcal"
            holder.calories.setTextColor(0xFFF44336.toInt()) // Red
            holder.icon.setImageResource(android.R.drawable.ic_media_play)
        }

        holder.deleteBtn.setOnClickListener { onDelete(entry.id) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_entry_name)
        val calories: TextView = view.findViewById(R.id.text_entry_calories)
        val icon: ImageView = view.findViewById(R.id.image_entry_type)
        val deleteBtn: ImageButton = view.findViewById(R.id.btn_delete_entry)
    }
}
