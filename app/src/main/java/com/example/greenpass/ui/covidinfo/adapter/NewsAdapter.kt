package com.example.greenpass.ui.covidinfo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.greenpass.R
import com.example.greenpass.data.model.ArticleWrapper
import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.News
import com.example.greenpass.ui.covidinfo.CovidInfoFragmentDirections
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class NewsAdapter(private val articles: ArrayList<ArticleWrapper>, var activity: FragmentActivity) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.bind(position, articles)
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_slide_from_bottom)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var img = item.findViewById<ImageView>(R.id.thumbnail)
        var title = item.findViewById<TextView>(R.id.news_title)
        var published = item.findViewById<TextView>(R.id.published)
        var newsType = item.findViewById<ImageView>(R.id.news_type)

        init{
            item.setOnClickListener{
                val action = CovidInfoFragmentDirections.newsView(articles[adapterPosition].article.link)
                activity.findNavController(R.id.nav_host_fragment).navigate(action)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(position: Int, articles: List<ArticleWrapper>){
            Picasso.get().load(articles[position].article.urlToImage).into(img, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception?) {
                    img.visibility = View.GONE
                }
            })
            title.text = articles[position].article.title
            published.text = "Published On: ${articles[position].article.pubDate.substring(0, 10)}"
            newsType.setImageResource(articles[position].icon)
        }
    }

    fun addArticles(articles: List<ArticleWrapper>){
        this.articles.apply {
            clear()
            addAll(articles)
        }
    }
}