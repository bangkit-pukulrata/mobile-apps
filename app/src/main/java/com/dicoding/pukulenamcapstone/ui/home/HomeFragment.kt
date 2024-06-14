package com.dicoding.pukulenamcapstone.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.pukulenamcapstone.data.News
import com.dicoding.pukulenamcapstone.adapter.NewsAdapter
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.adapter.LatestNewsAdapter
import com.dicoding.pukulenamcapstone.databinding.FragmentHomeBinding
import com.dicoding.pukulenamcapstone.viewmodel.LatestNewsViewModel
import com.dicoding.pukulenamcapstone.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LatestNewsViewModel
    private lateinit var adapter2: LatestNewsAdapter
    private lateinit var adapter: NewsAdapter

//    private val newsList = ArrayList<News>().apply {
//        add(News("Usulan Pembentukan Kabinet Prabowo dengan 40 Menteri", "Description 1", "https://img.antaranews.com/cache/800x533/2024/05/30/20240515122342_IMG_7142.jpg"))
//        add(News("Usulan Pembentukan Kabinet Prabowo dengan 40 Menteri", "Description 2", "https://img.antaranews.com/cache/800x533/2024/05/30/20240515122342_IMG_7142.jpg"))
//        add(News("Usulan Pembentukan Kabinet Prabowo dengan 40 Menteri", "Description 2", "https://img.antaranews.com/cache/800x533/2024/05/30/20240515122342_IMG_7142.jpg"))
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView16.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_allNewsFragment)
        }

        binding.button2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_allNewsFragment)
        }



        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[LatestNewsViewModel::class.java]

        adapter2 = LatestNewsAdapter(parentFragmentManager)
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHome.adapter = adapter2

        viewModel.getLatestNews("terbaru")

        viewModel.upRes.observe(viewLifecycleOwner) { response ->
            if (response.success == true) {
                Log.d("HomeFragment", "Latest news: ${response.data?.posts}")
                adapter2.submitList(response.data?.posts?.take(10))

                binding.viewPager.adapter = NewsAdapter(response.data?.posts?.take(3))

            } else {
                Log.d("HomeFragment", "Failed to fetch latest news: ${response.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
