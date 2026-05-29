package com.example.masterplan.ui.habits

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.masterplan.R
import java.time.LocalDate

class HabitsAdapter(
    private val onToggle: (Habit) -> Unit,
    private val onDelete: (Int) -> Unit
) : ListAdapter<Habit, HabitsAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(a: Habit, b: Habit) = a.id == b.id
            override fun areContentsTheSame(a: Habit, b: Habit) = a == b
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false) as CardView
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = getItem(position)
        val done = habit.completedDate == LocalDate.now().toString()

        holder.name.text = habit.name
        holder.checkbox.isChecked = done

        if (done) {
            holder.name.paintFlags = holder.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.name.alpha = 0.45f
            holder.card.setCardBackgroundColor(
                ContextCompat.getColor(holder.card.context, R.color.habit_card_done_background)
            )
        } else {
            holder.name.paintFlags = holder.name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.name.alpha = 1f
            holder.card.setCardBackgroundColor(
                ContextCompat.getColor(holder.card.context, R.color.habit_card_background)
            )
        }

        holder.card.setOnClickListener { onToggle(habit) }
        holder.deleteBtn.setOnClickListener { onDelete(habit.id) }
    }

    class ViewHolder(val card: CardView) : RecyclerView.ViewHolder(card) {
        val name: TextView = card.findViewById(R.id.text_habit_name)
        val checkbox: CheckBox = card.findViewById(R.id.checkbox_habit)
        val deleteBtn: ImageButton = card.findViewById(R.id.btn_delete_habit)
    }
}
