package com.dicoding.pukulenamcapstone.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.data.remote.response.PostsItem
import com.dicoding.pukulenamcapstone.databinding.ItemNewsBinding
import com.dicoding.pukulenamcapstone.ui.home.AllNewsFragment
import com.dicoding.pukulenamcapstone.ui.home.HomeFragment
import com.dicoding.pukulenamcapstone.utils.capitalizeWords
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class LatestNewsAdapter(private val fragmentManager: FragmentManager) : ListAdapter<PostsItem, LatestNewsAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LatestNewsAdapter.ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,fragmentManager)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostsItem>() {
            override fun areItemsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
                return oldItem.link == newItem.link
            }

            override fun areContentsTheSame(
                oldItem: PostsItem,
                newItem: PostsItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: LatestNewsAdapter.ViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem)
    }

    class ViewHolder(
        val binding: ItemNewsBinding,
        private val fragmentManager: FragmentManager
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: PostsItem) {
            val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(16))
            Log.d("ViewHolder", "Binding data: Title: ${newsItem.title}, PubDate: ${newsItem.pubDate}, Thumbnail: ${newsItem.thumbnail}")
            Glide.with(binding.root)
                .load(newsItem.thumbnail)
                .apply(requestOptions)
                .into(binding.imageView8)
            binding.newsTitle.text = newsItem.title
            binding.textView37.text = formatPubDate(newsItem.pubDate)
            binding.textView36.text = newsItem.category?.uppercase()  // Tambahkan ini
            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("title", newsItem.title)
                    putString("img", newsItem.thumbnail)
                    putString("desc", newsItem.description)
                    putString("date", formatPubDate(newsItem.pubDate))
                    putString("category", newsItem.category)
                }

                val currentFragment = fragmentManager.primaryNavigationFragment
                if (currentFragment is HomeFragment) {
                    Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_newsDetailFragment, bundle)
                } else if (currentFragment is AllNewsFragment) {
                    Navigation.findNavController(it).navigate(R.id.action_allNewsFragment_to_newsDetailFragment, bundle)
                }
            }
        }

        private fun formatPubDate(pubDate: String?): String {
            val zonedDateTime = ZonedDateTime.parse(pubDate, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
            return zonedDateTime.format(formatter)
        }
    }
}