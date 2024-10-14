package com.example.mousecats

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mousecats.ui.theme.MouseCatsTheme

class MainActivity : AppCompatActivity() {

    private var miceCount = 1
    private var speed = 1
    private var size = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekBarMiceCount = findViewById<SeekBar>(R.id.seekBarMiceCount)
        val seekBarSpeed = findViewById<SeekBar>(R.id.seekBarSpeed)
        val seekBarSize = findViewById<SeekBar>(R.id.seekBarSize)

        val miceCountText = findViewById<TextView>(R.id.miceCountText)
        val speedText = findViewById<TextView>(R.id.speedText)
        val sizeText = findViewById<TextView>(R.id.sizeText)
        val startButton = findViewById<Button>(R.id.startButton)
        val statButton = findViewById<Button>(R.id.statButton)

        seekBarMiceCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                miceCount = progress + 1
                miceCountText.text = "Количество мышей: $miceCount"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speed = progress + 1
                speedText.text = "Скорость: $speed"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                size = progress + 1
                sizeText.text = "Размер мышей: $size"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        startButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("miceCount", miceCount)
            intent.putExtra("speed", speed)
            intent.putExtra("size", size)
            startActivity(intent)
        }
        statButton.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }
    }
}
