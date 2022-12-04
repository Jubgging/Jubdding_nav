package com.example.jubgging_nav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.jubgging_nav.databinding.FragmentMainBinding
import com.example.jubgging_nav.databinding.FragmentRecordBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class MainFragment : Fragment() {

    companion object{
        var BaseUrl = "http://api.openweathermap.org/" // 주소
        var AppId = "9c6aa2d9071c3490f722cb006f3ca706" // api key
        var lat = "37.445293"
        var lon = "126.785823"
    }

    var i = 1
    lateinit var binding: FragmentMainBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    //binding되고 나서 불리는 함수
    // 버튼 눌려서 어떻게 할 지 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlogging.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_PloggingMapsFragment)
            // firebase chile 생성
            databaseReference.child("User").child(i.toString()).child("score").setValue(User().score)
            databaseReference.child("User").child(i++.toString()).child("time").setValue(User().time)

        }

        // 날씨 구현

        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("MainActivity", "result :" + t.message)
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if(response.code() == 200){
                    val weatherResponse = response.body()
                    Log.d("MainFragment", "result: " + weatherResponse.toString())

                    binding.tvName.text = weatherResponse!!.sys!!.name
                    binding.tvCountry.text = weatherResponse!!.sys!!.country
                    binding.tvMain.text = weatherResponse!!.weather!!.get(0).main
                    binding.tvDescription.text = weatherResponse!!.weather!!.get(0).description

                    var temp = weatherResponse!!.main!!.temp - 273.15
                    binding.tvTemp.text = String.format("%.1f °C", temp)


                    Glide.with(this@MainFragment)
                        .load(
                            resources.getDrawable(
                                resources.getIdentifier(
                                    "icon_" + weatherResponse!!.weather!!.get(0).icon,
                                    "drawable",
                                    activity?.packageName
                                )
                            )
                        )
                        .placeholder(R.drawable.weather)
                        .error(R.drawable.weather)
                        .fallback(R.drawable.weather)
                        .into(binding.ivWeather)

                }
            }
        })
    }
}

interface WeatherService{

    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String) :
            Call<WeatherResponse>
}

class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("main") var main: Main? = null
    @SerializedName("sys") var sys: Sys? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null // 날씨
    @SerializedName("description") var description: String? = null // 상세정보
    @SerializedName("icon") var icon : String? = null // 날씨 아이콘
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
}

class Sys {
    @SerializedName("country")
    var country: String? = null

    @SerializedName("name") // 도시이름
    var name: String? = null

}

