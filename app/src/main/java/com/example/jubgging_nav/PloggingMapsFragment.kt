package com.example.jubgging_nav

import android.content.ContentValues.TAG
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


class PloggingMapsFragment : Fragment(), OnMapReadyCallback {
    var scoreNumber = 1

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference


    lateinit var binding: FragmentMapsBinding
    lateinit var mapView: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.btnCamera.setOnClickListener {
            findNavController().navigate(R.id.action_PloggingMapsFragment_to_cameraFragment)
        }


        // 플로깅 점수 firebase 연동
        val myScore = firebaseDatabase.getReference("Score")

        myScore.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue()
                binding.txtPloggingScore.text = "${value}점"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        binding.btnStop.setOnClickListener {
            myScore.removeValue()
            findNavController().navigate(R.id.action_PloggingMapsFragment_to_recordFragment)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        // 서울 좌표 입력 후 카메라를 서울로 이동 시키고 10f 수준으로 줌시킴
        val seoul = LatLng(37.566, 126.978)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(seoul))
        googleMap?.moveCamera(CameraUpdateFactory.zoomTo(10f))

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


}
