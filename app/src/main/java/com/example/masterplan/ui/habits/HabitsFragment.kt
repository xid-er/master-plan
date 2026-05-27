package com.example.masterplan.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.masterplan.databinding.FragmentHabitsBinding

class HabitsFragment : Fragment() {

    private lateinit var habitsViewModel: HabitsViewModel
    private var _binding: FragmentHabitsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        habitsViewModel =
                ViewModelProvider(this).get(HabitsViewModel::class.java)

        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHabits
        habitsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}