package com.example.masterplan.ui.chores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.masterplan.databinding.FragmentChoresBinding

class ChoresFragment : Fragment() {

    private lateinit var choresViewModel: ChoresViewModel
    private var _binding: FragmentChoresBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        choresViewModel =
                ViewModelProvider(this).get(ChoresViewModel::class.java)

        _binding = FragmentChoresBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textChores
        choresViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}