package com.example.greenpass.ui.covidinfo.pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenpass.R
import com.example.greenpass.data.api.ApiHelper
import com.example.greenpass.data.api.ServiceBuilder
import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.Vaccine
import com.example.greenpass.ui.base.ViewModelFactory
import com.example.greenpass.ui.covidinfo.CovidInfoViewModel
import com.example.greenpass.ui.covidinfo.adapter.ExploreAdapter
import com.example.greenpass.ui.covidinfo.adapter.VaccineAdapter
import com.example.greenpass.utils.Status
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_explore.country_recycler
import kotlinx.android.synthetic.main.fragment_explore.progress
import kotlinx.android.synthetic.main.fragment_vaccine.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VaccineFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class VaccineFrag : Fragment() {

    private lateinit var viewModel: CovidInfoViewModel
    private lateinit var adapter: VaccineAdapter

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        setupStuff()
    }

    private fun setupStuff() {
        //Set up ViewModel
        viewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(ServiceBuilder.apiService))
        ).get(CovidInfoViewModel::class.java)

        //setupUI
        adapter = VaccineAdapter(Vaccine(arrayListOf(), listOf(), "", ""))
        vaccine_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@VaccineFrag.adapter
        }

        //setup observers
        viewModel.getVaccines().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        vaccine_recycler.visibility = View.VISIBLE
                        vaccine_progress.visibility = View.GONE
                        resource.data?.let { vaccine -> retrieveList(vaccine) }
                    }
                    Status.ERROR -> {
                        vaccine_recycler.visibility = View.VISIBLE
                        vaccine_progress.visibility = View.GONE
                        it.message?.let { it1 -> Log.d("debug", it1) }
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        vaccine_progress.visibility = View.VISIBLE
                        vaccine_recycler.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(vaccine: Vaccine){
        adapter.apply {
            addVaccines(vaccine)
            notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VaccineFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VaccineFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}