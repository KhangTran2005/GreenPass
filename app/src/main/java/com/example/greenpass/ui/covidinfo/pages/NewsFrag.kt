package com.example.greenpass.ui.covidinfo.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenpass.R
import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.News
import com.example.greenpass.ui.covidinfo.adapter.ExploreAdapter
import com.example.greenpass.ui.covidinfo.adapter.NewsAdapter
import com.example.greenpass.ui.covidinfo.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_explore.*

class NewsFrag : Fragment() {

    private val viewModel: NewsViewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(News(listOf()))

        //TODO: change the recycler thing

        country_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@NewsFrag.adapter
        }

        viewModel.responseVaccine.observe(viewLifecycleOwner, {
            updateData(it)
            country_progress.visibility = View.GONE
        })
    }

    private fun updateData(news: News){
    }

}