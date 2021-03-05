package com.example.greenpass.ui.covidinfo.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenpass.R
import com.example.greenpass.data.model.Country
import com.example.greenpass.ui.covidinfo.adapter.ExploreAdapter
import com.example.greenpass.ui.covidinfo.viewmodel.ExploreViewModel
import kotlinx.android.synthetic.main.fragment_explore.*


class ExploreFrag : Fragment() {
    private val viewModel: ExploreViewModel by lazy {
        ViewModelProvider(this).get(ExploreViewModel::class.java)
    }
    private lateinit var adapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExploreAdapter(arrayListOf())

        country_progress.visibility = View.GONE

        country_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ExploreFrag.adapter
        }

        viewModel.response.observe(viewLifecycleOwner, {
            updateData(it)
        })

//        viewModel.reponseFake.observe(viewLifecycleOwner, {
//            country_textview.text = it
//        })
    }

    private fun updateData(countries: List<Country>){
        adapter.addCountries(countries)
        adapter.notifyDataSetChanged()
    }
}