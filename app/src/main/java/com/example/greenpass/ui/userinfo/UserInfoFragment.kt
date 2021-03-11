package com.example.greenpass.ui.userinfo

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.greenpass.R
import com.example.greenpass.ui.base.InfoDialog
import com.example.greenpass.utils.Clearance
import com.example.greenpass.utils.Particulars
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_userinfo.*

class UserInfoFragment : Fragment(){

    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var user_name: TextView
    private lateinit var user_clearance: TextView
    private lateinit var user_vacc_date: TextView
    private val username: String by lazy {
        Particulars.getUsername(requireContext()) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_userinfo, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initializing field text views
        user_name = name.getChildAt(0) as TextView
        user_clearance = clearance.getChildAt(0) as TextView
        user_vacc_date  = vacc_date.getChildAt(0) as TextView

        var age = 0
        var vacc_place = ""

        //get user data from Firebase
        Firebase.database.reference
            .child("users")
            .child(username)
            .get().addOnSuccessListener {user ->
                user_name.text = user.child("name").value.toString()
                user_clearance.text = user.child("clearance_level").value.toString()
                user_vacc_date.text = user.child("vaccination_date").value.toString()
                age = user.child("age").value.toString().toInt()
                vacc_place = user.child("vaccination_place").value.toString()
            }

        //set up on click listeners for descriptions on cards
        name.setOnClickListener{
            InfoDialog(user_name.text.toString(), "Age: $age").show(childFragmentManager, InfoDialog.TAG)
        }
        clearance.setOnClickListener{
            InfoDialog("${user_clearance.text}", Clearance.getDesc("${user_clearance.text}")).show(childFragmentManager, InfoDialog.TAG)
        }
        vacc_date.setOnClickListener{
            InfoDialog("${user_vacc_date.text}", "Vaccinated at: $vacc_place").show(childFragmentManager, InfoDialog.TAG)
        }

    }
}