package com.example.masterplan.ui.calories

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.masterplan.R
import com.example.masterplan.databinding.FragmentCaloriesBinding
import java.util.Locale

class CaloriesFragment : Fragment() {

    private var _binding: FragmentCaloriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CaloriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaloriesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CaloriesViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CalorieEntriesAdapter { id -> viewModel.deleteEntry(id) }
        binding.recyclerCalories.adapter = adapter

        viewModel.entries.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.goalAndRemaining.observe(viewLifecycleOwner) { (goal, remaining) ->
            binding.textGoalValue.text = "$goal kcal"
            binding.textRemainingValue.text = String.format(Locale.getDefault(), "%,d", remaining)
            
            if (remaining < 0) {
                binding.textRemainingValue.setTextColor(0xFFF44336.toInt()) // Red
            } else {
                binding.textRemainingValue.setTextColor(0xFF4CAF50.toInt()) // Green
            }
        }

        binding.btnEditGoal.setOnClickListener { showEditGoalDialog() }
        binding.fabAddMeal.setOnClickListener { showAddEntryDialog(EntryType.MEAL) }
        binding.fabAddExercise.setOnClickListener { showAddEntryDialog(EntryType.EXERCISE) }
    }

    private fun showEditGoalDialog() {
        val input = EditText(requireContext()).apply {
            hint = "2000"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.set_daily_goal)
            .setView(input)
            .setPositiveButton(R.string.save) { _, _ ->
                val goal = input.text.toString().toIntOrNull() ?: return@setPositiveButton
                viewModel.setGoal(goal)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showAddEntryDialog(type: EntryType) {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 0)
        }
        val nameInput = EditText(requireContext()).apply { hint = "Name (e.g. Lunch)" }
        val calInput = EditText(requireContext()).apply {
            hint = "Calories"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        layout.addView(nameInput)
        layout.addView(calInput)

        AlertDialog.Builder(requireContext())
            .setTitle(if (type == EntryType.MEAL) R.string.add_meal else R.string.add_exercise)
            .setView(layout)
            .setPositiveButton(R.string.add) { _, _ ->
                val name = nameInput.text.toString().ifBlank { "Untitled" }
                val calories = calInput.text.toString().toIntOrNull() ?: 0
                viewModel.addEntry(name, calories, type)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
