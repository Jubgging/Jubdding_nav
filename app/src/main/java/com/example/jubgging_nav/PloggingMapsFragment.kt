package com.example.jubgging_nav

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jubgging_nav.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Timer
import kotlin.concurrent.timer


class PloggingMapsFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentMapsBinding
    private lateinit var ploggingTime : String // 플로깅 시간

    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) { //Context 할당
        super.onAttach(context)
        mainActivity = context as MainActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    private var time = 0
    private var timerTask: Timer? = null

    //타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 10) {
            time++

            val min = time / 6000
            val sec = (time % 6000) / 100

            mainActivity.runOnUiThread {
                ploggingTime = "$min 분 $sec 초"
                binding.txtTime.text = ploggingTime

            }
        }
    }

    //타이머 멈춤
    private fun stopTimer() {
        timerTask?.cancel()
    }

    lateinit var mapView: MapView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.btnStart.setOnClickListener {
            startTimer()
        }


        binding.btnCamera.setOnClickListener {
            findNavController().navigate(R.id.action_PloggingMapsFragment_to_cameraFragment)
        }


        // 플로깅 점수 firebase 연동
        val myScore = firebaseDatabase.getReference("User").child(i.toString())
            .child("score")

        myScore.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                binding.txtPloggingScore.text = "${value}점"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "FireBase 연동에 실패했습니다..", error.toException())
            }
        })

        binding.btnStop.setOnClickListener {
            databaseReference.child("User").child(i++.toString()).child("time").setValue(ploggingTime)
            findNavController().navigate(R.id.action_PloggingMapsFragment_to_recordFragment)
            score = 0
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        // 고양시 좌표 입력 후 카메라를 서울로 이동 시키고 10f 수준으로 줌시킴
        val seoul = LatLng(37.599, 126.865)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(seoul))
        googleMap?.moveCamera(CameraUpdateFactory.zoomTo(10f))

        val marker =
            MarkerOptions()
                .position(seoul)
                .title("경기도 고양시")
                .snippet("한국항공대학교")
        googleMap?.addMarker(marker)
    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

}
