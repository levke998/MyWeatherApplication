package com.sl.myweatherapplication.Activity

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sl.myweatherapplication.R
import com.sl.myweatherapplication.ViewModel.CountdownRingView
import com.sl.myweatherapplication.databinding.ActivityCountDownBinding
import com.sl.myweatherapplication.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {

    private lateinit var countdownRingView: CountdownRingView
    lateinit var binding: ActivityCountDownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        binding=ActivityCountDownBinding.inflate(layoutInflater)

        countdownRingView = findViewById(R.id.countdownRingView)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        startCountdown()
    }

    private fun startCountdown() {
        // Karácsony dátuma
        val calendarEnd = Calendar.getInstance()
        calendarEnd.set(Calendar.MONTH, Calendar.DECEMBER)
        calendarEnd.set(Calendar.DAY_OF_MONTH, 25)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 0)
        calendarEnd.set(Calendar.MINUTE, 0)
        calendarEnd.set(Calendar.SECOND, 0)

        // Hátralévő idő milliszekundumban
        val currentTime = System.currentTimeMillis()
        val christmasTime = calendarEnd.timeInMillis
        val timeUntilChristmas = christmasTime - currentTime

        // CountDownTimer beállítása
        object : CountDownTimer(timeUntilChristmas, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                binding.napTxt.text = days.toString()
                binding.oraTxt.text = hours.toString()
                binding.percTxt.text = minutes.toString()
                binding.masodpercTxt.text = seconds.toString()

                countdownRingView.updateCountdown(days, hours, minutes, seconds)
            }

            override fun onFinish() {
                // Ha elérkezik a karácsony, megjeleníthetünk egy üzenetet vagy animációt
            }
        }.start()
    }
}
