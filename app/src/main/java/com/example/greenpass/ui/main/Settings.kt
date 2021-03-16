package com.example.greenpass.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.data.model.User
import com.example.greenpass.utils.Clearance
import com.example.greenpass.utils.Particulars
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment() {

    private lateinit var mCallback: LogIn.OnLogInListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mCallback = activity as LogIn.OnLogInListener
        }
        catch(e: ClassCastException){
            throw java.lang.ClassCastException(activity.toString() + "must implement OnLogInListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debug_btn.setOnClickListener{
            Firebase.database.reference
                .child("users")
                .child("user_new")
                .setValue(User("name", "id", "age", "dob", "nationality", "sex"))
        }

        logout_card.setOnClickListener{
            Particulars.writeUserName(null, requireContext())
            Database.username = ""
            Particulars.writeUserInfo(null, requireContext())
            mCallback.disableAdmin()
            val action = SettingsDirections.logout()
            (requireActivity() as LogIn.OnLogInListener).lockDrawer()
            findNavController().navigate(action)
        }
    }
}