package com.example.greenpass.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.utils.Particulars
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_log_in.*
import org.mindrot.jbcrypt.BCrypt

class LogIn : Fragment() {
    private lateinit var mCallback: OnLogInListener

    interface OnLogInListener{
        fun unlockDrawer()

        fun lockDrawer()
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

        // TODO: Default is username:"admin",password:"password"

        login_btn.setOnClickListener{

            val username = user_input.text.toString()
            val password = password_input.text.toString()

            Firebase.database.reference
                    .child("usernameToPassword")
                    .child(username)
                    .get().addOnSuccessListener {
                        if (!it.exists() || !BCrypt.checkpw(password,it.value as String)){
                            Toast.makeText(requireContext(),"Invalid username or password",Toast.LENGTH_SHORT).show()
                        } else{
                            Database.username = user_input.text.toString()
                            Particulars.writeUserName(Database.username,requireContext())

                            val action = LogInDirections.loginAccepted()
                            mCallback.unlockDrawer()
                            findNavController().navigate(action)
                        }
                    }
        }
    }
}