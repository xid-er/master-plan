package com.example.masterplan.ui.chores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masterplan.R
import com.example.masterplan.databinding.FragmentChoresBinding

class ChoresFragment : Fragment() {

    private lateinit var viewModel: ChoresViewModel
    private var _binding: FragmentChoresBinding? = null
    private val binding get() = _binding!!

    private val adapter = ChoresAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ChoresViewModel::class.java)
        _binding = FragmentChoresBinding.inflate(inflater, container, false)

        binding.recyclerChores.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerChores.adapter = adapter

        viewModel.chores.observe(viewLifecycleOwner) { adapter.submitList(it.map { c -> c.name }) }

        viewModel.randomChore.observe(viewLifecycleOwner) { chore ->
            binding.textRandomResult.visibility = if (chore != null) View.VISIBLE else View.GONE
            binding.textRandomResult.text = chore?.let { getString(R.string.random_result, it) }
        }

        binding.btnRandomChore.setOnClickListener { viewModel.pickRandom() }
        binding.floatingActionButton.setOnClickListener { showAddDialog() }

        return binding.root
    }

    private fun showAddDialog() {
        val input = EditText(requireContext()).apply { hint = getString(R.string.chore_hint) }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_chore)
            .setView(input)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val text = input.text.toString().trim()
                if (text.isNotEmpty()) viewModel.addChore(text)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
