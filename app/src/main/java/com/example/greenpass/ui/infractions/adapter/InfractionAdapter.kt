package com.example.greenpass.ui.infractions.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.greenpass.R
import com.example.greenpass.ui.covidinfo.CovidInfoFragmentDirections
import com.example.greenpass.utils.InfractionRecord
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.StringBuilder

class InfractionAdapter(private var infractions: ArrayList<InfractionRecord>, var activity: FragmentActivity): RecyclerView.Adapter<InfractionAdapter.ViewHolder>() {

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var title = item.findViewById<TextView>(R.id.infraction_location_nameField)
        var date = item.findViewById<TextView>(R.id.infraction_dateField)

        fun bind(position: Int, infractions: List<InfractionRecord>){
            title.text = infractions[position].locationName
            date.text = infractions[position].getReadableDate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.infraction_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, infractions)
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_slide_from_bottom)
    }

    override fun getItemCount(): Int {
        return infractions.size
    }

    fun addInfractions(infractions: List<InfractionRecord>){
        this.infractions.apply {
            clear()
            addAll(infractions)
        }
    }
}