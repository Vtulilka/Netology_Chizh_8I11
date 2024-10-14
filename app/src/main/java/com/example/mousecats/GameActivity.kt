package com.example.mousecats

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var dbHelper: StatsDatabaseHelper
    private var startTime: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private val runnableList = mutableListOf<Runnable>()
    private var miceCount = 1
    private var speed = 1
    private var size = 1
    private val screenWidth by lazy { resources.displayMetrics.widthPixels }
    private val screenHeight by lazy { resources.displayMetrics.heightPixels }

    // Переменные для отслеживания кликов
    private var totalClicks = -1
    private var mouseClicks = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        dbHelper = StatsDatabaseHelper(this)

        // Запоминаем время начала игры
        startTime = System.currentTimeMillis()

        // Получаем параметры из MainActivity
        miceCount = intent.getIntExtra("miceCount", 1)
        speed = intent.getIntExtra("speed", 1)
        size = intent.getIntExtra("size", 1)

        // Устанавливаем слушатель на все касания экрана
        findViewById<android.widget.FrameLayout>(R.id.gameFrame).setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                totalClicks++
                Log.d("GameActivity", "Total Clicks: $totalClicks")
            }
            true
        }

        // Добавляем нужное количество мышек
        for (i in 0 until miceCount) {
            createMouse()
        }
    }

    // Создаем мышку и добавляем её на экран
    private fun createMouse() {
        val mouse = ImageView(this).apply {
            setImageResource(R.drawable.mouse1) // Изображение мышки
            layoutParams = android.widget.FrameLayout.LayoutParams(
                size * 100, size * 100 // Размер мышки в зависимости от выбора пользователя
            )
        }

        // Добавляем мышку на экран
        findViewById<android.widget.FrameLayout>(R.id.gameFrame).addView(mouse)

        // Устанавливаем слушатель кликов на мышку
        mouse.setOnClickListener {
            mouseClicks++
            Log.d("GameActivity", "Mouse Clicks: $mouseClicks")
        }

        // Запускаем бесконечное движение мышки
        moveMouse(mouse)
    }

    // Функция, которая анимирует движение мышки по экрану
    private fun moveMouse(mouse: ImageView) {
        val startX = Random.nextInt(0, screenWidth - mouse.width)
        val startY = Random.nextInt(0, screenHeight - mouse.height)

        // Устанавливаем мышку в случайную начальную точку
        mouse.x = startX.toFloat()
        mouse.y = startY.toFloat()

        // Функция для передвижения мышки к случайной точке
        val runnable = object : Runnable {
            override fun run() {
                val targetX = Random.nextInt(0, screenWidth - mouse.width)
                val targetY = Random.nextInt(0, screenHeight - mouse.height)

                // Анимация перемещения мышки
                ObjectAnimator.ofFloat(mouse, View.X, targetX.toFloat()).apply {
                    duration = (5000 / speed).toLong() // Скорость анимации зависит от параметра speed
                    start()
                }
                ObjectAnimator.ofFloat(mouse, View.Y, targetY.toFloat()).apply {
                    duration = (5000 / speed).toLong()
                    start()
                }

                // Повторяем движение после завершения анимации
                handler.postDelayed(this, (5000 / speed).toLong())
            }
        }

        runnableList.add(runnable)
        handler.post(runnable)
    }
    private fun saveGameResult() {
        val endTime = System.currentTimeMillis()
        val gameTime = (endTime - startTime) / 1000 // Время игры в секундах

        // Рассчитываем процент попаданий
        val accuracy = if (totalClicks + mouseClicks > 0) (mouseClicks.toFloat() / (totalClicks + mouseClicks) * 100).toInt() else 0

        // Добавляем результат игры в базу данных
        dbHelper.addGame(totalClicks + mouseClicks, mouseClicks, accuracy, "$gameTime сек")
    }
    // Обрабатываем нажатие кнопки "Back" для выхода из игры
    override fun onBackPressed() {
        saveGameResult()
        // Очищаем все действия, связанные с мышками
        runnableList.forEach { handler.removeCallbacks(it) }
        super.onBackPressed()
    }
}