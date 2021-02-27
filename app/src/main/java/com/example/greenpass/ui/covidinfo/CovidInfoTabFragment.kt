package com.example.greenpass.ui.covidinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.greenpass.R
import com.example.greenpass.ui.covidinfo.pages.BlankFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.covid_info_tab_fragment.*

class CovidInfoTabFragment : Fragment() {

    companion object {
        fun newInstance() = CovidInfoTabFragment()
    }

    private lateinit var viewModel: CovidInfoTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.covid_info_tab_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        covid_info_viewPager.adapter = CovidInfoTabAdapter(this)
        TabLayoutMediator(covid_info_tabLayout, covid_info_viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CovidInfoTabViewModel::class.java)
    }

    class CovidInfoTabAdapter(fm: Fragment) : FragmentStateAdapter(fm) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                else -> BlankFragment()
            }
        }
    }
}