package com.example.greenpass.data.model

import com.example.greenpass.R

data class ArticleWrapper(
        var article: News.Article,
        var icon: Int
){

    companion object{
        fun toWrapper(articles: List<News.Article>, type: Int): List<ArticleWrapper>{
            val articleWrappers: ArrayList<ArticleWrapper> = arrayListOf()
            when (type){
                1 -> for (article in articles){
                    articleWrappers.add(ArticleWrapper(article, R.drawable.ic_baseline_colorize_24))
                }

                2 -> for (article in articles){
                    articleWrappers.add(ArticleWrapper(article, R.drawable.health))
                }

                3 -> for (article in articles){
                    articleWrappers.add(ArticleWrapper(article, R.drawable.ic_baseline_coronavirus_24))
                }
            }

            return articleWrappers
        }
    }
}