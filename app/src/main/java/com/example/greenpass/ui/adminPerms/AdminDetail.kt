package com.example.greenpass.ui.adminPerms

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.data.model.User
import com.example.greenpass.utils.AdminDialog
import com.example.greenpass.utils.Clearance
import com.example.greenpass.utils.PieDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_admin_detail.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AdminDetail : Fragment() {

    private lateinit var user: User
    private lateinit var username: String
    private lateinit var base: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        user = Json.decodeFromString(requireArguments().getString("user").toString())

        return inflater.inflate(R.layout.fragment_admin_detail, container, false)
    }

    fun updateVaccs(date: String, place: String){
        Log.d("debug", "$date $place")
        vacc_place_tv.text = place
        vacc_time_tv.text = date
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name_tv.text = user.name
        id_tv.text = user.ID

        if (!user.vacc_date.equals("Not Vaccinated")){
            vaccination_switch.isChecked = true
        }
        if (user.clearance == Clearance.ADMIN){
            admin_switch.isChecked = true
            cop_switch.isChecked = false
            Database.user?.let {
                if (user.name == it.name){
                    admin_switch.isEnabled = false
                }
            }
        }
        else if (user.clearance == Clearance.OFFICER){
            cop_switch.isChecked = true
            cop_switch.isChecked = false
        }

        if (user.vacc_date == "Not Vaccinated"){
            vacc_place_tv.text = "NIL"
            vacc_time_tv.text = "NIL"
        }
        else{
            vacc_place_tv.text = user.vacc_loc
            vacc_time_tv.text = user.vacc_date
        }

        Firebase.database.reference
            .child("nameToUsername")
            .child(user.name).get().addOnSuccessListener {
                username = it.value.toString()
                base = Firebase.database.reference
                    .child("users")
                    .child(username)

                Log.d("debug", username)

                cop_switch.setOnCheckedChangeListener{buttonView, isChecked ->
                    if (isChecked){
                        base.child("clearance_level").setValue(2)
                    }
                    else {
                        base.child("clearance_level").setValue(0)
                    }
                }

                admin_switch.setOnCheckedChangeListener{buttonView, isChecked ->
                    if (isChecked){
                        base.child("clearance_level").setValue(3)
                    }
                    else {
                        if (user.vacc_date != "Not Vaccinated"){
                            base.child("clearance_level").setValue(1)
                        }
                        else{
                            base.child("clearance_level").setValue(0)
                        }
                    }
                }

                vaccination_switch.setOnCheckedChangeListener{ buttonView, isChecked ->
                    if (isChecked) {
                        AdminDialog(user, this).show(childFragmentManager, AdminDialog.TAG)
                    }
                    else{
                        base.child("vaccination_date").setValue("Not Vaccinated")
                        base.child("vaccination_place").setValue("Go to your nearest approved clinic for vaccination")
                        base.child("clearance_level").setValue(0)
                        vacc_place_tv.text = "NIL"
                        vacc_time_tv.text = "NIL"
                    }
                }
            }
    }
}