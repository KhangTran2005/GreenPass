package com.example.greenpass.ui.covidinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenpass.data.repository.MainRepo
import com.example.greenpass.utils.Resource
import kotlinx.coroutines.Dispatchers

class CovidInfoViewModel(private val mainRepo: MainRepo) : ViewModel() {

    fun getCountries() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try{
            emit(Resource.success(data = mainRepo.getCountries()))
        }
        catch(exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?:"Error Occurred"))
        }
    }
}