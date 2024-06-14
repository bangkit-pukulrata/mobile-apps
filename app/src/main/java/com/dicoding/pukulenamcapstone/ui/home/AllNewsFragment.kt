package com.dicoding.pukulenamcapstone.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.adapter.LatestNewsAdapter
import com.dicoding.pukulenamcapstone.databinding.FragmentAllNewsBinding
import com.dicoding.pukulenamcapstone.databinding.FragmentHomeBinding
import com.dicoding.pukulenamcapstone.viewmodel.LatestNewsViewModel
import com.dicoding.pukulenamcapstone.viewmodel.ViewModelFactory
import com.google.android.material.chip.Chip
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AllNewsFragment : Fragment() {
    private var _binding: FragmentAllNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LatestNewsViewModel
    private lateinit var adapter2: LatestNewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_allNewsFragment_to_homeFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAllNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[LatestNewsViewModel::class.java]

        adapter2 = LatestNewsAdapter(parentFragmentManager)
        binding.rvAll.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAll.adapter = adapter2

        viewModel.getLatestNews("terbaru")
        val firstChip = binding.chipGroup.getChildAt(0) as Chip
        firstChip.isChecked = true
        viewModel.getLatestNews(firstChip.text.toString().lowercase())

        viewModel.upRes.observe(viewLifecycleOwner) { response ->
            if (response.success == true) {
                Log.d("HomeFragment", "Latest news: ${response.data?.posts}")
                adapter2.submitList(response.data?.posts)
            } else {
                Log.d("HomeFragment", "Failed to fetch latest news: ${response.message}")
            }
        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null) {
                val lowercase = chip.text.toString().lowercase()
                viewModel.getLatestNews(lowercase)
            }
        }
    }


}