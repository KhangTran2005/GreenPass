package com.example.greenpass.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val _username = MutableLiveData("")
    private val _password = MutableLiveData("")

    val userName: LiveData<String>
        get() = _username
    val password: LiveData<String>
        get() = _password

    fun setUserName(username: String){
        _username.value = username
    }

    fun setPassword(password: String){
        _password.value = password
    }
}