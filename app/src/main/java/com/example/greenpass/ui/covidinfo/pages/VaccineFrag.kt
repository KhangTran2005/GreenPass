package com.example.greenpass.ui.covidinfo.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenpass.R
import com.example.greenpass.data.model.Vaccine
import com.example.greenpass.ui.covidinfo.adapter.VaccineAdapter
import com.example.greenpass.ui.covidinfo.viewmodel.ExploreViewModel
import com.example.greenpass.ui.covidinfo.viewmodel.VaccineViewModel
import kotlinx.android.synthetic.main.fragment_vaccine.*

class VaccineFrag : Fragment() {

    private lateinit var adapter: VaccineAdapter
    private val viewModel: VaccineViewModel by lazy {
        ViewModelProvider(this).get(VaccineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vaccine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VaccineAdapter(Vaccine(listOf(), listOf(), "", ""))

        vaccine_progress.visibility = View.GONE

        vaccine_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@VaccineFrag.adapter
        }

        viewModel.response.observe(viewLifecycleOwner, {
            updateData(it)
        })
    }

    private fun updateData(vaccine: Vaccine){
        adapter.addVaccines(vaccine)
        adapter.notifyDataSetChanged()
    }
}