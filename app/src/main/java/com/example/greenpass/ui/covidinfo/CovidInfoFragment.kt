package com.example.greenpass.ui.covidinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.greenpass.R
import kotlinx.android.synthetic.main.fragment_covidinfo.*

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
        val adapter = PagerAdapter(childFragmentManager, bottom_navigation.menu.size())
        pager.adapter = adapter
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.explore -> pager.currentItem = 0
                R.id.vaccine -> pager.currentItem = 1
                R.id.news -> pager.currentItem = 2
            }
            true
        }

        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position){
                    0 -> {
                        bottom_navigation.menu.findItem(R.id.explore).isChecked = true
                    }
                    1 -> {
                        bottom_navigation.menu.findItem(R.id.vaccine).isChecked = true
                    }
                    2 -> {
                        bottom_navigation.menu.findItem(R.id.news).isChecked = true
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}