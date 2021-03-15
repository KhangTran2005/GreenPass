package com.example.greenpass.ui.covidinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.greenpass.R
import com.example.greenpass.data.model.Country
import com.example.greenpass.ui.covidinfo.CovidInfoFragmentDirections
import com.example.greenpass.ui.covidinfo.pages.ExploreFrag
import com.google.android.material.transition.MaterialElevationScale
import com.squareup.picasso.Picasso
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExploreAdapter(private val countries: ArrayList<Country>, var activity: FragmentActivity, ) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {


    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        var flag = item.findViewById<ImageView>(R.id.country_flag)
        var title = item.findViewById<TextView>(R.id.country_title)
        var cases = item.findViewById<TextView>(R.id.cases)

        init{
            item.setOnClickListener{ view ->
                item.transitionName = activity.getString(R.string.country_card_transition_name, adapterPosition.toString())
                val countryTransName = activity.getString(R.string.country_card_detail_transition_name)
                val extras = FragmentNavigatorExtras(view to countryTransName)
                val action = CovidInfoFragmentDirections.countryDetail(Json.encodeToString(countries[adapterPosition]))
                activity.findNavController(R.id.nav_host_fragment).navigate(action, extras)
            }
        }

        fun bind (country: Country){
            cases.text = "Cases: ${country.cases}"
            title.text = country.country
            Picasso.get().load(country.countryInfo.flag).into(flag)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_card, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_slide_from_bottom)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun addCountries(countries: List<Country>){
        this.countries.apply {
            clear()
            addAll(countries)
        }
    }
}