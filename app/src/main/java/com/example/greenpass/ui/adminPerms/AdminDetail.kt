package com.example.greenpass.ui.adminPerms

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenpass.R
import com.example.greenpass.data.model.User
import com.example.greenpass.utils.AdminDialog
import com.example.greenpass.utils.PieDialog
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_admin_detail.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AdminDetail : Fragment() {

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        user = Json.decodeFromString(requireArguments().getString("user").toString())

        return inflater.inflate(R.layout.fragment_admin_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name_tv.text = user.name
        id_tv.text = user.ID

        if (!user.vacc_date.equals("Not Vaccinated")){
            vaccination_switch.isChecked = true
        }

        vaccination_switch.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                AdminDialog(user).show(childFragmentManager, AdminDialog.TAG)
            }
            else{
                Firebase.database.reference
                    .child("nameToUsername")
                    .child(user.name).get().addOnSuccessListener {
                        val base = Firebase.database.reference
                            .child("users")
                            .child(it.value as String)

                        base.child("vaccination_date").setValue("Not Vaccinated")
                        base.child("vaccination_place").setValue("Go to your nearest approved clinic for vaccination")
                        base.child("clearance_level").setValue(0)
                    }
            }
        }
    }
}