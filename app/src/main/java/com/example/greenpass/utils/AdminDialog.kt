package com.example.greenpass.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.data.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_admin_dialog.*

class AdminDialog(var user: User) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getDialog() != null && getDialog()?.getWindow() != null) {
            getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.fragment_admin_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        confirmThing.setOnClickListener {

            val vacc_date = vaccination_date_et.text.toString()
            val vacc_place = vaccination_place_et.text.toString()

            Firebase.database.reference
                .child("nameToUsername")
                .child(user.name).get().addOnSuccessListener {
                    val base = Firebase.database.reference
                        .child("users")
                        .child(it.value as String)

                    base.child("vaccination_date").setValue(vacc_date)
                    base.child("vaccination_place").setValue(vacc_place)

                    base.child("clearance_level").get()
                        .addOnSuccessListener {
                            if (it.value.toString().toInt() == 0){
                                base.child("clearance_level").setValue(1)
                            }
                        }

                    if (Database.username.equals(it.value.toString())){
                        Database.updateUser()
                    }
                }
            Toast.makeText(requireContext(), "Info Submitted", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    companion object{
        const val TAG = "admin_dialog"
    }
}