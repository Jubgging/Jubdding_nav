package com.example.jubgging_nav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class User(val time: String = "", val score: Int = 0)

class PloggingViewModel : ViewModel() {
    private val repo = PloggingsRepository()

    fun fetchData(): LiveData<MutableList<User>> {
        val mutableData  = MutableLiveData<MutableList<User>>()

        repo.getData().observeForever {

            mutableData.value = it
        }
        return mutableData
    }


}