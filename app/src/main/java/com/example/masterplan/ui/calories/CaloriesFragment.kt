package com.example.masterplan.ui.calories

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.masterplan.R
import com.example.masterplan.databinding.DialogAddCalorieEntryBinding
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
        val dialogBinding = DialogAddCalorieEntryBinding.inflate(layoutInflater)
        dialogBinding.textInputLayoutName.visibility = View.GONE
        dialogBinding.textInputLayoutCalories.hint = getString(R.string.set_daily_goal)
        dialogBinding.editCalories.setText("")
        dialogBinding.editCalories.hint = "2000"
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.set_daily_goal)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                val goal = dialogBinding.editCalories.text.toString().toIntOrNull() ?: return@setPositiveButton
                viewModel.setGoal(goal)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
        dialogBinding.editCalories.requestFocus()
        
        dialogBinding.editCalories.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
                true
            } else false
        }
    }

    private fun showAddEntryDialog(type: EntryType) {
        val dialogBinding = DialogAddCalorieEntryBinding.inflate(layoutInflater)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(if (type == EntryType.MEAL) R.string.add_meal else R.string.add_exercise)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.add) { _, _ ->
                val name = dialogBinding.editName.text.toString().ifBlank { "Untitled" }
                val calories = dialogBinding.editCalories.text.toString().toIntOrNull() ?: 0
                viewModel.addEntry(name, calories, type)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
        dialogBinding.editName.requestFocus()

        dialogBinding.editCalories.setOnEditorActionListener { _, actionId, _ ->
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
