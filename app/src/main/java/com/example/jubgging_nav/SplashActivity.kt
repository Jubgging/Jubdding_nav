package com.example.jubgging_nav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.jubgging_nav.databinding.ActivitySplashBinding

//메인 화면
class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    //뷰가 생성되었을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root) //그릴 xml 뷰 파일을 연결 시켜준다.

        // 스플래시 화면 2초 뒤에 MainPage 화면으로 전환

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)

    }
    companion object {
        private const val DURATION : Long = 3000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}