package com.example.jubgging_nav

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PloggingsRepository {

    fun getData(): LiveData<MutableList<User>> {
        val mutableData = MutableLiveData<MutableList<User>>()
        val database = Firebase.database
        val myRef = database.getReference("User")

        myRef.addValueEventListener(object : ValueEventListener {
            val listData : MutableList<User> = mutableListOf<User>()

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    listData.clear() // 실시간 데이터 업데이트 시 리사이클러뷰 데이터 중복방지

                    for (userSnapshot in snapshot.children) {

                        val getData = userSnapshot.getValue(User::class.java)
                        listData.add(getData!!)

                        mutableData.value = listData
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류시
                Log.e("error : ", "firebase 오류")
            }
        })
        return mutableData
    }
}