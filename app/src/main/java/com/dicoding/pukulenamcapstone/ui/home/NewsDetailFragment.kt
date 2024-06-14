package com.dicoding.pukulenamcapstone.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.databinding.FragmentHomeBinding
import com.dicoding.pukulenamcapstone.databinding.FragmentNewsDetailBinding
import kotlin.math.log

class NewsDetailFragment : Fragment() {
    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_newsDetailFragment_to_homeFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val title = it.getString("title")
            val img = it.getString("img")
            val desc = it.getString("desc")
            val date = it.getString("date")
            val category = it.getString("category")

            Log.d("NewsDetailFragment", "Title: $category")

            binding.textView27.text = title
            binding.textView32.text = title
            binding.textView39.text = desc
            if (category != null) {
                binding.button3.text = category.uppercase()
            }

            Log.d("NewsDetailFragment", "Title: $desc")
            Glide.with(this).load(img).centerCrop().into(binding.imageView10)
        }
    }
}