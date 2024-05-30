package com.dicoding.pukulenamcapstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.data.News
import com.github.islamkhsh.CardSliderAdapter

class NewsAdapter(private val newsList: ArrayList<News>) : CardSliderAdapter<NewsAdapter.NewsViewHolder>() {

    override fun getItemCount() = newsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_carousel, parent, false)
        return NewsViewHolder(view)
    }

    override fun bindVH(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)
    }

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView17)
        private val titleTextView: TextView = view.findViewById(R.id.textView43)
//        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

        fun bind(news: News) {
            titleTextView.text = news.title
//            descriptionTextView.text = news.description
//            imageView.setImageResource(R.drawable.ic_launcher_background)
//            imageView.setImageResource(R.drawable.news_placeholder)
            Glide.with(itemView.context).load(news.imageUrl).into(imageView)
        }
    }
}
