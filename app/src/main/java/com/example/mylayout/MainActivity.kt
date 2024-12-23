package com.example.mylayout

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var timeLeftTextView: TextView // TextView для відображення часу
    private val handler = Handler(Looper.getMainLooper()) // Для періодичного оновлення

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Знаходимо кнопку "Расписание" по ID
        val scheduleButton: Button = findViewById(R.id.scheduleButton)
        timeLeftTextView = findViewById(R.id.suma) // TextView для часу до наступної пари

        // Переход на екран з розкладом
        scheduleButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        // Запускаємо оновлення часу
        startUpdatingTime()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Очищуємо Handler при знищенні активності
    }

    private fun startUpdatingTime() {
        // Оновлюємо текст кожну секунду
        handler.post(object : Runnable {
            override fun run() {
                timeLeftTextView.text = calculateTimeLeft() // Оновлюємо текст
                handler.postDelayed(this, 1000) // Повторно запускаємо через 1 секунду
            }
        })
    }

    // Функція для обчислення часу до наступної пари
    private fun calculateTimeLeft(): String {
        val currentTime = Calendar.getInstance()
        val dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val schedule = mapOf(
            Calendar.MONDAY to listOf("15:45", "17:15", "18:45"),
            Calendar.TUESDAY to listOf("15:45", "17:15", "18:45"),
            Calendar.WEDNESDAY to listOf("14:00", "15:45", "17:15"),
            Calendar.THURSDAY to listOf("14:00", "15:45", "17:15"),
            Calendar.FRIDAY to listOf("12:15", "14:00", "15:45", "17:15")
        )

        val todaySchedule = schedule[dayOfWeek] ?: return "Вихідний"

        for (time in todaySchedule) {
            val pairTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, time.split(":")[0].toInt())
                set(Calendar.MINUTE, time.split(":")[1].toInt())
                set(Calendar.SECOND, 0)
            }

            if (currentTime.before(pairTime)) {
                val diff = pairTime.timeInMillis - currentTime.timeInMillis
                val hours = diff / (1000 * 60 * 60)
                val minutes = (diff / (1000 * 60)) % 60
                val seconds = (diff / 1000) % 60
                return "Залишилось $hours год. $minutes хв. $seconds сек."
            }
        }

        // Якщо всі пари завершились, шукаємо першу пару наступного дня
        var nextDay = (dayOfWeek % 7) + 1
        while (schedule[nextDay] == null) {
            nextDay = (nextDay % 7) + 1
        }

        val nextDaySchedule = schedule[nextDay]!!
        val firstPairTime = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, (nextDay - dayOfWeek + 7) % 7)
            set(Calendar.HOUR_OF_DAY, nextDaySchedule.first().split(":")[0].toInt())
            set(Calendar.MINUTE, nextDaySchedule.first().split(":")[1].toInt())
            set(Calendar.SECOND, 0)
        }

        val diff = firstPairTime.timeInMillis - currentTime.timeInMillis
        val hours = diff / (1000 * 60 * 60)
        val minutes = (diff / (1000 * 60)) % 60
        val seconds = (diff / 1000) % 60
        return "До наступної пари: $hours год. $minutes хв. $seconds сек."
    }
}
