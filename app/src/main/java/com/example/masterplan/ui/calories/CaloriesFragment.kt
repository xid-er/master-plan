package com.example.masterplan.ui.calories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.masterplan.databinding.FragmentCaloriesBinding

class CaloriesFragment : Fragment() {

    private lateinit var caloriesViewModel: CaloriesViewModel
    private var _binding: FragmentCaloriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        caloriesViewModel =
                ViewModelProvider(this).get(CaloriesViewModel::class.java)

        _binding = FragmentCaloriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCalories
        caloriesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}