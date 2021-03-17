package com.example.greenpass.ui.adminPerms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenpass.R
import com.example.greenpass.data.model.Country
import com.example.greenpass.data.model.User
import com.example.greenpass.utils.Clearance
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_admin.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class Admin : Fragment() {

    private lateinit var adapter: FirebaseRecyclerAdapter<User, Admin.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val baseRef = Firebase.database.reference.child("users")

        val options = FirebaseRecyclerOptions.Builder<User>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(baseRef){
                val name = it.child("name").value.toString()
                val clearance = Clearance.findByValue(
                    it.child("clearance_level").value.toString().toInt()
                )
                val vacc_date = it.child("vaccination_date").value.toString()
                val age = it.child("age").value.toString()
                val vacc_loc = it.child("vaccination_place").value.toString()
                val DoB = it.child("DoB").value.toString()
                val ID = it.child("ID").value.toString()
                val nationality = it.child("Nationality").value.toString()
                val sex = it.child("Sex").value.toString()

                User(name, ID, age, DoB, nationality, sex, clearance!!, vacc_date, vacc_loc)
            }.build()

        adapter = object: FirebaseRecyclerAdapter<User, ViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val viewH = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_card, parent,false)
                return ViewHolder(viewH)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: User) {
                holder.bind(model)
                holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_slide_from_bottom)
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(requireContext(), "An error occured loading the data", Toast.LENGTH_SHORT).show()
            }
        }

        admin_recycler.apply {
            adapter = this@Admin.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        var name: TextView = item.findViewById(R.id.vaccine_title)
        var id: TextView = item.findViewById(R.id.sponsor)

        fun bind (user: User){

            itemView.setOnClickListener{
                val action = AdminDirections.toDetail(Json.encodeToString(user))
                findNavController().navigate(action)
            }

            name.text = user.name
            id.text = user.ID
        }
    }
}