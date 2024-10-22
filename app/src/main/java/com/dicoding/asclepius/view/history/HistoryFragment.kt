package com.dicoding.asclepius.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        val factory = HistoryFactory.getInstance(requireContext())
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        historyViewModel.cancers.observe(viewLifecycleOwner) {cancerList->
            historyAdapter.submitList(cancerList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}