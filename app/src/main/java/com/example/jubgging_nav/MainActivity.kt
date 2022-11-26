package com.example.jubgging_nav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.jubgging_nav.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var miBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //Splash 화면
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)

        miBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(miBinding.root)

        //네비게이션 담을 호스트가져오기
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션 컨트롤러
        val navController = navHostFragment.navController

        //바텀 네비게이션 뷰와 네비게이션의 연결 !
        NavigationUI.setupWithNavController(miBinding.myBottomNav, navController)


    }
}