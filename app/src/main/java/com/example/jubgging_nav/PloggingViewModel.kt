package com.example.jubgging_nav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class User(val time: String = "None", var score: Long = 0) {

    operator fun inc(): User {
        return User(time, score.inc())
    }
}


class PloggingViewModel : ViewModel() { //안드로이드 livecyle의 viewmodel 상속 받아서 씀
    private val repo = PloggingsRepository()

    fun fetchData(): LiveData<MutableList<User>> {
        val mutableData  = MutableLiveData<MutableList<User>>()

        repo.getData().observeForever {

            mutableData.value = it
        }
        return mutableData
    }
}

/*
* class MbtiViewModel: ViewModel() {
*   private val _mbti = MutableLiveData<String>(초기화값)
*   val mbti : LiveData<String> get() = _mbti ** 밖에서 데이터 함부로 건들지 못하게 정보 은닉
*
*
*
*
*
*
*
* */