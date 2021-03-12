package com.example.greenpass.ui.covidinfo.detailed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenpass.R
import kotlinx.android.synthetic.main.fragment_news_view.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NewsView : Fragment() {

    private lateinit var link: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        link = requireArguments().getString("link").toString()

        return inflater.inflate(R.layout.fragment_news_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webview.loadUrl(link)
    }
}