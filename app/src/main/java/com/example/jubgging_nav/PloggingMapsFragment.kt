package com.example.jubgging_nav

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

    private lateinit var ploggingTime : String // 플로깅 시간
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference


    // 현재위치정보 받아오기
    var mLocationManager : LocationManager ?= null
    var mLocationListener : LocationListener ?= null

    // onAttach() 콜백메서드에서 Context를 MainActivity로 형변환하여 할당

    lateinit var mainActivity: MainActivity // context를 할당할 변수를 프로퍼티로 선언
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        // mainActivity.runOnUIThread 이런식으로 사용하면 된다..
    }

    private var time = 0
    private var timerTask: Timer? = null

    lateinit var binding: FragmentMapsBinding
    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLocationManager = mainActivity.getSystemService(LOCATION_SERVICE) as LocationManager
        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location : Location) {
                var lat = 0.0
                var lng = 0.0
                if (location != null) {
                    lat = location.latitude
                    lng = location.longitude
                    Log.d("현재 위도와 경도는", "위도 : ${lat}, 경도 : ${lng} 입니다.")
                }
                var currentLocation = LatLng(lat, lng)
                mapView.getMapAsync {
                    it!!.addMarker(MarkerOptions().position(currentLocation).title("현재위치"))
                    it!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.btnStart.setOnClickListener {
            nowPlace()
            //startTimer()


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
            // 서울 좌표 입력 후 카메라를 서울로 이동 시키고 20f 수준으로 줌시킴
            val seoul = LatLng(37.566, 126.978)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(seoul))
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(15f))

            val marker =
                MarkerOptions()
                    .position(seoul)
                    .title("서울")
                    .snippet("아름다운 도시")
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


    // 현재위치 업데이트
    fun nowPlace() {
        if (ContextCompat.checkSelfPermission(
                mainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                mainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000L,
                30f,
                mLocationListener!!
            )
        }

    }



}
