package com.example.greenpass.ui.infractions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.data.model.Vaccine
import com.example.greenpass.ui.covidinfo.adapter.VaccineAdapter
import com.example.greenpass.ui.covidinfo.viewmodel.VaccineViewModel
import com.example.greenpass.ui.infractions.adapter.InfractionAdapter
import com.example.greenpass.utils.InfractionRecord
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_vaccine.*
import kotlinx.android.synthetic.main.infraction_fragment.*

class InfractionFragment : Fragment() {

    companion object {
        fun newInstance() = InfractionFragment()
    }

    private lateinit var adapter: InfractionAdapter
    private val viewModel: InfractionViewModel by lazy {
        ViewModelProvider(this).get(InfractionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.infraction_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = InfractionAdapter(arrayListOf(), requireActivity())

        infraction_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@InfractionFragment.adapter
        }

        fetchInfractions()
    }

    fun updateData(infractions: List<InfractionRecord>){
        adapter.addInfractions(infractions)
        adapter.notifyDataSetChanged()
    }

    fun fetchInfractions(){
        Firebase.database.reference
            .child("users")
            .child(Database.username)
            .child("infractions")
            .child("N").get().addOnSuccessListener {
                val N = it.value.toString().toInt()
                fun rec(i: Int, res: List<InfractionRecord>){
                    if(i == N) {
                        updateData(res.sortedBy { it.date })
                        infraction_progress.visibility = View.INVISIBLE
                        if(N == 0){
                            no_infraction_text.visibility = View.VISIBLE
                        }
                    } else {
                        Firebase.database.reference
                            .child("users")
                            .child(Database.username)
                            .child("infractions")
                            .child(i.toString()).get().addOnSuccessListener {
                                val infract = InfractionRecord(
                                    it.child("location").value.toString(),
                                    it.child("time").value.toString()
                                )
                                println(infract.date)
                                rec(i+1,res+infract)
                            }
                    }
                }
                rec(0, listOf())
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        no_infraction_text.visibility = View.INVISIBLE
    }

}