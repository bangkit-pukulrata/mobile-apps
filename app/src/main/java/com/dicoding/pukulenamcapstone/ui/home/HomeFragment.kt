package com.dicoding.pukulenamcapstone.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import com.dicoding.pukulenamcapstone.data.News
import com.dicoding.pukulenamcapstone.adapter.NewsAdapter
import com.dicoding.pukulenamcapstone.R
import com.github.islamkhsh.CardSliderViewPager

class HomeFragment : Fragment() {

    private lateinit var adapter: NewsAdapter
    private val newsList = ArrayList<News>().apply {
        add(News("Usulan Pembentukan Kabinet Prabowo dengan 40 Menteri", "Description 1", "https://img.antaranews.com/cache/800x533/2024/05/30/20240515122342_IMG_7142.jpg"))
        add(News("Usulan Pembentukan Kabinet Prabowo dengan 40 Menteri", "Description 2", "https://img.antaranews.com/cache/800x533/2024/05/30/20240515122342_IMG_7142.jpg"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<TextView>(R.id.textView16)
        button.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_allNewsFragment)
        }

        val button2 = view.findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_allNewsFragment)
        }

        val newsItem = view.findViewById<ViewGroup>(R.id.included_item_news)
        newsItem.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_newsDetailFragment)
        }

        val viewPager = view.findViewById<CardSliderViewPager>(R.id.viewPager)
        adapter = NewsAdapter(newsList)
        viewPager.adapter = adapter
    }
}
