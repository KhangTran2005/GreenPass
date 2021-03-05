package com.example.greenpass.ui.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.greenpass.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogIn : Fragment() {
    private lateinit var mCallback: OnLogInListener

    interface OnLogInListener{
        fun onLogInListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mCallback = activity as OnLogInListener
        }
        catch(e: ClassCastException){
            throw java.lang.ClassCastException(activity.toString() + "must implement OnDialogDismissListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn.setOnClickListener{
            //TODO: Do log in things
            val action = LogInDirections.loginAccepted()
            mCallback.onLogInListener()
            findNavController().navigate(action)
        }
    }
}