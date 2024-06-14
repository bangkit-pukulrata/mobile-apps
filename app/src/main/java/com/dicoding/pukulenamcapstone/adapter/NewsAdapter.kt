package com.dicoding.pukulenamcapstone.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.data.remote.response.PostsItem
import com.github.islamkhsh.CardSliderAdapter
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class NewsAdapter(private val newsList: List<PostsItem?>?) : CardSliderAdapter<NewsAdapter.NewsViewHolder>() {

    override fun getItemCount(): Int = newsList?.size!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_carousel, parent, false)
        return NewsViewHolder(view)
    }

    override fun bindVH(holder: NewsViewHolder, position: Int) {
        val news = newsList?.get(position)
        if (news != null) {
            holder.bind(news)
        }
    }

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView17)
        private val titleTextView: TextView = view.findViewById(R.id.textView43)

        fun bind(news: PostsItem) {
            titleTextView.text = news.title
            Glide.with(itemView.context).load(news.thumbnail).into(imageView)

            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("title", news.title)
                    putString("img", news.thumbnail)
                    putString("desc", news.description)
                    putString("date", formatPubDate(news.pubDate))
                }
                Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_newsDetailFragment, bundle)
            }
        }
        private fun formatPubDate(pubDate: String?): String {
            val zonedDateTime = ZonedDateTime.parse(pubDate, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
            return zonedDateTime.format(formatter)
        }
    }

}

