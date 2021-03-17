package com.example.greenpass.ui.register

import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.greenpass.R
import com.example.greenpass.data.Database
import com.example.greenpass.data.model.User
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.math.exp


class Register : Fragment() {
    var user_new: User? = null
    private val regiViewModel: RegisterViewModel by lazy{
        ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
    }
    private var isExpanded = false

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

        expandDialog.setOnClickListener{
            if (isExpanded){
                collapse()
            }
            else {
                expand()
            }

            isExpanded = !isExpanded
        }

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
            expand()
            isExpanded = true

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
            if (fieldsNotNull()){
                user_new = User(name_input.text.toString(), nric_input.text.toString(), age_input.text.toString(), DoB_input.text.toString(), nationality_input.text.toString(), sex_input.text.toString())
            }
            if (!(TextUtils.isEmpty(user_regis_input.text) || TextUtils.isEmpty(password_regis_input.text)))
            user_new?.let {
                Database.addNewUser(user_regis_input.text.toString(), password_regis_input.text.toString(), it)
                regiViewModel.setPassword("")
                regiViewModel.setUserName("")
                val action = RegisterDirections.redirectToLogIn()
                findNavController().navigate(action)
            }
        }
    }

    private fun expand(){
        name_layout.visibility = View.VISIBLE
        age_layout.visibility = View.VISIBLE
        dob_layout.visibility = View.VISIBLE
        gender_layout.visibility = View.VISIBLE
        nric_layout.visibility = View.VISIBLE
        nationality_layout.visibility = View.VISIBLE

        expandDialog.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
    }

    private fun collapse(){
        name_layout.visibility = View.GONE
        age_layout.visibility = View.GONE
        dob_layout.visibility = View.GONE
        gender_layout.visibility = View.GONE
        nric_layout.visibility = View.GONE
        nationality_layout.visibility = View.GONE

        expandDialog.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
    }

    private fun fieldsNotNull(): Boolean{
        return !(TextUtils.isEmpty(name_input.text) || TextUtils.isEmpty(age_input.text) || TextUtils.isEmpty(DoB_input.text) || TextUtils.isEmpty(nric_input.text)
                || TextUtils.isEmpty(nationality_input.text) || TextUtils.isEmpty(sex_input.text))
    }
}