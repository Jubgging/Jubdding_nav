package com.example.jubgging_nav

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import android.window.SplashScreen
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.jubgging_nav.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 11 // 카메라 권한 퍼미션

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Jubgging_nav)
        super.onCreate(savedInstanceState)

        // splash activity 없이 screen 구현

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //네비게이션 담을 호스트가져오기
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션 컨트롤러
        val navController = navHostFragment.navController

        //바텀 네비게이션 뷰와 네비게이션의 연결 !
        NavigationUI.setupWithNavController(binding.myBottomNav, navController)


        // 카메라 권한체크
        checkPermission() //권한체크

    }

    // 지도 위치 권한 확인
    fun checkPermission() {
        val permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val permissionRead =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWrite =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        //권한이 없으면 권한 요청
        if (permissionCamera != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED || permissionWrite != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "앱을 실행하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) { //fragment 로 전달
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }



}