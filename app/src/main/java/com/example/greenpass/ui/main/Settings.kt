package com.example.greenpass.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.utils.Particulars
import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout_card.setOnClickListener{
            Particulars.writeUserName(null, requireContext())
            Database.username = ""
            val action = SettingsDirections.logout()
            (requireActivity() as LogIn.OnLogInListener).lockDrawer()
            findNavController().navigate(action)
        }
    }
}