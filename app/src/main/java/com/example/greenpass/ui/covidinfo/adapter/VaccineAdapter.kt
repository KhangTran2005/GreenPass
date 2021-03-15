package com.example.greenpass.ui.covidinfo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.greenpass.R
import com.example.greenpass.data.model.Vaccine
import com.example.greenpass.ui.covidinfo.CovidInfoFragmentDirections
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.StringBuilder

class VaccineAdapter(private var vaccines: ArrayList<Vaccine.Data>, var activity: FragmentActivity): RecyclerView.Adapter<VaccineAdapter.ViewHolder>() {

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var title = item.findViewById<TextView>(R.id.vaccine_title)
        var sponsor = item.findViewById<TextView>(R.id.sponsor)

        init{
            item.setOnClickListener{ view ->
                item.transitionName = activity.getString(R.string.vaccine_card_transition_name, adapterPosition.toString())
                val vaccineTransName = activity.getString(R.string.vaccine_card_detail_transition_name)
                val extras = FragmentNavigatorExtras(view to vaccineTransName)
                val action = CovidInfoFragmentDirections.vaccineDetail(Json.encodeToString(vaccines[adapterPosition]))
                activity.findNavController(R.id.nav_host_fragment).navigate(action, extras)
            }
        }

        fun bind(position: Int, vaccines: List<Vaccine.Data>){
            title.text = vaccines[position].candidate
            val str = StringBuilder()
            for (i in 0..(vaccines[position].sponsors.size - 2)){
                str.append(vaccines[position].sponsors[i]).append(",")
            }
            sponsor.text = str.append(vaccines[position].sponsors[vaccines[position].sponsors.size - 1]).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("debug", "Onbind: $position")
        holder.bind(position, vaccines)
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_slide_from_bottom)
    }

    override fun getItemCount(): Int {
        return vaccines.size
    }

    fun addVaccines(vaccines: List<Vaccine.Data>){
        this.vaccines.apply {
            clear()
            addAll(vaccines)
        }
    }
}