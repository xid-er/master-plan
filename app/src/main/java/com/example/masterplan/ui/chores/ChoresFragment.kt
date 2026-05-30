package com.example.masterplan.ui.chores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masterplan.R
import com.example.masterplan.databinding.DialogGenericInputBinding
import com.example.masterplan.databinding.FragmentChoresBinding

class ChoresFragment : Fragment() {

    private lateinit var viewModel: ChoresViewModel
    private var _binding: FragmentChoresBinding? = null
    private val binding get() = _binding!!

    private val adapter = ChoresAdapter { id -> viewModel.deleteChore(id) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ChoresViewModel::class.java)
        _binding = FragmentChoresBinding.inflate(inflater, container, false)

        binding.recyclerChores.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerChores.adapter = adapter

        viewModel.chores.observe(viewLifecycleOwner) { adapter.submitList(it) }

        viewModel.randomChore.observe(viewLifecycleOwner) { chore ->
            binding.textRandomResult.visibility = if (chore != null) View.VISIBLE else View.GONE
            binding.textRandomResult.text = chore?.let { getString(R.string.random_result, it) }
        }

        binding.btnRandomChore.setOnClickListener { viewModel.pickRandom() }
        binding.floatingActionButton.setOnClickListener { showAddDialog() }

        return binding.root
    }

    private fun showAddDialog() {
        val dialogBinding = DialogGenericInputBinding.inflate(layoutInflater)
        dialogBinding.textInputLayout.hint = getString(R.string.chore_hint)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_chore)
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val text = dialogBinding.editInput.text.toString().trim()
                if (text.isNotEmpty()) viewModel.addChore(text)
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
