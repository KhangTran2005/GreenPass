package com.example.greenpass.ui.covidinfo.detailed

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenpass.R
import com.example.greenpass.data.model.Country
import com.example.greenpass.utils.CasesDialog
import com.example.greenpass.utils.PieDialog
import com.example.greenpass.utils.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_country_detail.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.eazegraph.lib.models.PieModel

class CountryDetail : Fragment() {

    private lateinit var country: Country

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        country = Json.decodeFromString(requireArguments().getString("countryString").toString())
        return inflater.inflate(R.layout.fragment_country_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPage()

    }

    @SuppressLint("SetTextI18n")
    private fun setUpPage(){
        country_name.text = country.country
        continent.text = country.continent
        population.text = "Population: ${country.population}"
        cases_tv.text = country.cases.toString()
        vactest_tv.text = country.tests.toString()

        piechart.addPieSlice(PieModel("Active", country.active.toFloat(), Color.RED))
        piechart.addPieSlice(PieModel("Deaths", country.deaths.toFloat(), Color.GRAY))
        piechart.addPieSlice(PieModel("Recovered", country.recovered.toFloat(), Color.GREEN))


        pieCard.setOnClickListener{v ->
            PieDialog(country.active, country.deaths, country.recovered).show(childFragmentManager, PieDialog.TAG)
        }

        casesCard.setOnClickListener{v ->
            CasesDialog(country.todayCases, country.todayDeaths, country.todayRecovered).show(childFragmentManager, CasesDialog.TAG)
        }
    }
}