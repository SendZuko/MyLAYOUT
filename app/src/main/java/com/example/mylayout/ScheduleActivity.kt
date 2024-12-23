package com.example.mylayout

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule) // Указываем разметку второго экрана

        // Находим кнопку "Назад" по ID
        val backButton: Button = findViewById(R.id.backButton)

        // Устанавливаем обработчик клика для возврата на предыдущий экран
        backButton.setOnClickListener {
            finish() // Закрывает текущую активность и возвращает на предыдущий экран
        }
    }
}

