package com.dicoding.pukulenamcapstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.data.RelatedNews

class RelatedNewsAdapter(private val newsList: List<RelatedNews>) :
    RecyclerView.Adapter<RelatedNewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.related_news_title)
        val isFakeTextView: TextView = itemView.findViewById(R.id.related_news_status)
        val constraint: View = itemView.findViewById(R.id.constraint_related_news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_related_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.titleTextView.text = news.title
        holder.isFakeTextView.text = if (news.isFake == 1L) "Hoax" else "Bukan Hoax"
        holder.constraint.background = if (news.isFake == 1L) holder.itemView.context.getDrawable(R.drawable.chip_bg_red) else holder.itemView.context.getDrawable(R.drawable.chip_bg_blue)
    }

    override fun getItemCount() = newsList.size
}