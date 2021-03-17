package com.example.greenpass.ui.userinfo

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.ui.base.InfoDialog
import com.example.greenpass.ui.main.LogIn
import com.example.greenpass.utils.Clearance
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_userinfo.*

class UserInfoFragment : Fragment(){
    private lateinit var mCallback: CallBack
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var user_name: TextView
    private lateinit var user_clearance: TextView
    private lateinit var user_vacc_date: TextView

    interface CallBack{
        fun setNavView(name: String, id: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mCallback = activity as CallBack
        }
        catch(e: ClassCastException){
            throw java.lang.ClassCastException(activity.toString() + "must implement UserInfoFragment.CallBack")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_userinfo, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_sign_out).isVisible = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initializing field text views
        user_name = name.getChildAt(0) as TextView
        user_clearance = clearance.getChildAt(0) as TextView
        user_vacc_date  = vacc_date.getChildAt(0) as TextView

        //get user data from shared preference
        Database.user?.let {
            user_name.text = it.name
            user_clearance.text = it.clearance.toString()
            user_vacc_date.text = it.vacc_date
            val age = it.age
            val vacc_loc = it.vacc_loc
            val DoB = it.DoB
            val ID = it.ID
            val nationality = it.nationality
            val sex = it.sex

            mCallback.setNavView(it.name, it.ID)

            //set up on click listeners for descriptions on cards
            name.setOnClickListener{
                InfoDialog(user_name.text.toString(),
                    "ID: $ID\nAge: $age\nDoB: $DoB\nNationality: $nationality \nSex: $sex"
                ).show(childFragmentManager, InfoDialog.TAG)
            }
            clearance.setOnClickListener{
                InfoDialog("${user_clearance.text}", Clearance.getDesc("${user_clearance.text}")).show(childFragmentManager, InfoDialog.TAG)
            }
            vacc_date.setOnClickListener{
                InfoDialog("${user_vacc_date.text}", "Vaccinated at: $vacc_loc").show(childFragmentManager, InfoDialog.TAG)
            }
        }
    }
}