package com.example.masterplan.ui.habits

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masterplan.R
import com.example.masterplan.databinding.DialogGenericInputBinding
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

        binding.fabAddHabit.setOnClickListener { showAddHabitDialog() }

        return binding.root
    }

    private fun showAddHabitDialog() {
        val dialogBinding = DialogGenericInputBinding.inflate(layoutInflater)
        dialogBinding.textInputLayout.hint = getString(R.string.habit_hint)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_habit)
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val text = dialogBinding.editInput.text.toString().trim()
                if (text.isNotEmpty()) viewModel.addHabit(text)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()

        dialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
        dialogBinding.editInput.requestFocus()

        dialogBinding.editInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
                true
            } else false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
