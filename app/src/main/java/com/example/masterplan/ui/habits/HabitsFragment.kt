package com.example.masterplan.ui.habits

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masterplan.databinding.FragmentHabitsBinding

class HabitsFragment : Fragment() {

    private lateinit var viewModel: HabitsViewModel
    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    private val adapter = HabitsAdapter(
        onToggle = { viewModel.toggleHabit(it) },
        onDelete = { viewModel.deleteHabit(it) }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HabitsViewModel::class.java)
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)

        binding.recyclerHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHabits.adapter = adapter

        viewModel.habits.observe(viewLifecycleOwner) { adapter.submitList(it) }

        binding.fabAddHabit.setOnClickListener {
            val input = EditText(requireContext()).apply { hint = "Habit name" }
            AlertDialog.Builder(requireContext())
                .setTitle("Add Habit")
                .setView(input)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val text = input.text.toString().trim()
                    if (text.isNotEmpty()) viewModel.addHabit(text)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
