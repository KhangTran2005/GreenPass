package com.example.greenpass.ui.covidinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenpass.R
import com.example.greenpass.data.api.ApiHelper
import com.example.greenpass.data.api.ServiceBuilder
import com.example.greenpass.data.model.Country
import com.example.greenpass.ui.base.ViewModelFactory
import com.example.greenpass.ui.covidinfo.adapter.ExploreAdapter
import com.example.greenpass.ui.covidinfo.adapter.PagerAdapter
import com.example.greenpass.utils.Status
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_covidinfo.*
import kotlinx.android.synthetic.main.fragment_explore.*

class CovidInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_covidinfo, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureLayout()
    }

    private fun configureLayout(){
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.explore))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_baseline_colorize_24))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.news))

        val adapter = PagerAdapter(childFragmentManager, tab_layout.tabCount)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }
}