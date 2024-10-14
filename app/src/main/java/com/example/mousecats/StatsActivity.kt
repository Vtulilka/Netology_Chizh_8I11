package com.example.mousecats

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StatsActivity : AppCompatActivity() {

    private lateinit var dbHelper: StatsDatabaseHelper
    private lateinit var statsRecyclerView: RecyclerView
    private lateinit var statsAdapter: StatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        dbHelper = StatsDatabaseHelper(this)
        statsRecyclerView = findViewById(R.id.statsRecyclerView)

        val games = dbHelper.getLastTenGames()

        statsRecyclerView.layoutManager =LinearLayoutManager(this)
        statsAdapter = StatsAdapter(games)
        statsRecyclerView.adapter = statsAdapter
    }
}