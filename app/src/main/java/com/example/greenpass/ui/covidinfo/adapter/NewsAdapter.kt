package com.example.greenpass.ui.covidinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenpass.R
import com.example.greenpass.data.model.News
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class NewsAdapter(private val news: News) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.bind(position, news)
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_slide_from_bottom)
    }

    override fun getItemCount(): Int {
        return news.news.size
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var img = item.findViewById<ImageView>(R.id.thumbnail)
        var title = item.findViewById<TextView>(R.id.news_title)
        var published = item.findViewById<TextView>(R.id.published)

        fun bind(position: Int, news: News){
            Picasso.get().load(news.news[position].urlToImage).into(img, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception?) {
                    img.visibility = View.GONE
                }
            })
            title.text = news.news[position].title
            published.text = "Published On: ${news.news[position].pubDate.substring(0, 10)}"
        }
    }
}