package com.example.greenpass.data.model

import kotlinx.serialization.Serializable

data class News(
        val news: List<Article>
) {
    @Serializable
    data class Article(
            val content: String,
            val imageFileName: String?,
            val imageInLocalStorage: String,
            val link: String,
            val news_id: String,
            val pubDate: String,
            val reference: String,
            val title: String,
            val urlToImage: String
    )
}