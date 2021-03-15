package com.example.greenpass.ui.covidinfo.detailed

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenpass.R
import com.example.greenpass.data.model.Vaccine
import com.example.greenpass.ui.covidinfo.CovidInfoFragmentDirections
import com.example.greenpass.utils.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_vaccine_detail.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.StringBuilder

class VaccineDetail : Fragment() {

    private lateinit var vaccineData: Vaccine.Data

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

        vaccineData = Json.decodeFromString(requireArguments().getString("vaccineString").toString())

        return inflater.inflate(R.layout.fragment_vaccine_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPage()
    }

    private fun setUpPage(){
        candidate.text = vaccineData.candidate

        val ins_str = StringBuilder()
        for (i in 0..(vaccineData.institutions.size - 2)){
            ins_str.append(vaccineData.institutions[i]).append(", ")
        }
        ins_str.append(vaccineData.institutions.last())
        institution.text = "by ${ins_str.toString()}"

        val str = StringBuilder()
        for (i in 0..(vaccineData.sponsors.size - 2)){
            str.append(vaccineData.sponsors[i]).append(", ")
        }
        str.append(vaccineData.sponsors.last())
        sponsor.text = "sponsored by ${str.toString()}"

        mechanism.text = vaccineData.mechanism

        phase.text = vaccineData.trialPhase

        details_candidate.text = Html.fromHtml(vaccineData.details , Html.FROM_HTML_MODE_LEGACY)
    }
}