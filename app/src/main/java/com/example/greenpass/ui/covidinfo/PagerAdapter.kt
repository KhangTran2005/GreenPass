package com.example.greenpass.ui.covidinfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.greenpass.ui.covidinfo.pages.ExploreFrag
import com.example.greenpass.ui.covidinfo.pages.NewsFrag
import com.example.greenpass.ui.covidinfo.pages.VaccineFrag


class PagerAdapter (fm: FragmentManager, numberOfTabs: Int): FragmentPagerAdapter(fm, numberOfTabs) {
    var tabCount: Int = numberOfTabs

    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> ExploreFrag()
            1 -> VaccineFrag()
            2 -> NewsFrag()
            else -> Fragment()
        }
    }
}