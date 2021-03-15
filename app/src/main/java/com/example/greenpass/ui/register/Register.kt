package com.example.greenpass.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.data.model.User
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class Register : Fragment() {
    var user_new: User? = null
    private val regiViewModel: RegisterViewModel by lazy{
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        if (requireArguments().getString("user_new") != null){
            user_new = Json.decodeFromString(requireArguments().getString("user_new").toString())
        }

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_regis_input.setText(regiViewModel.userName.value)
        password_regis_input.setText(regiViewModel.password.value)

        user_regis_input.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                regiViewModel.setUserName(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        password_regis_input.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                regiViewModel.setPassword(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        user_new?.let {
            name_layout.visibility = View.VISIBLE
            age_layout.visibility = View.VISIBLE
            dob_layout.visibility = View.VISIBLE
            gender_layout.visibility = View.VISIBLE
            nric_layout.visibility = View.VISIBLE
            nationality_layout.visibility = View.VISIBLE

            name_input.setText(it.name)
            age_input.setText(it.age)
            DoB_input.setText(it.DoB)
            sex_input.setText(it.sex)
            nationality_input.setText(it.nationality)
            nric_input.setText(it.ID)
        }

        scan_btn.setOnClickListener{
            val action = RegisterDirections.gotoScan()
            findNavController().navigate(action)
        }

        register_btn.setOnClickListener{
            user_new?.let {
                Database.addNewUser(user_regis_input.text.toString(), password_regis_input.text.toString(), it)
                val action = RegisterDirections.redirectToLogIn()
                findNavController().navigate(action)
            }
        }
    }
}