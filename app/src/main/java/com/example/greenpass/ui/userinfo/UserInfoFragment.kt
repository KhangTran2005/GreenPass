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
import kotlinx.android.synthetic.main.fragment_userinfo.*

class UserInfoFragment : Fragment(){

    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var user_name: TextView
    private lateinit var user_clearance: TextView
    private lateinit var user_vacc_date: TextView

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

        //set up on click listeners for descriptions on cards
        name.setOnClickListener{
            InfoDialog("Insert Name", "Have content like age and stuff").show(childFragmentManager, InfoDialog.TAG)
        }
        clearance.setOnClickListener{
            InfoDialog("Insert Clearance Level", "Have the descriptions of perms allowed by the clearance level in question").show(childFragmentManager, InfoDialog.TAG)
        }
        vacc_date.setOnClickListener{
            InfoDialog("Vaccination Date", "Maybe have extra content like where the vaccination took place").show(childFragmentManager, InfoDialog.TAG)
        }

    }
}