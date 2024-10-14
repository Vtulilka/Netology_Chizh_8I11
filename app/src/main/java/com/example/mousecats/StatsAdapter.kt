package com.example.mousecats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatsAdapter(private val games: List<GameStats>) : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameTimeTextView: TextView = itemView.findViewById(R.id.gameTimeTextView)
        val totalClicksTextView: TextView = itemView.findViewById(R.id.totalClicksTextView)
        val mouseClicksTextView: TextView = itemView.findViewById(R.id.mouseClicksTextView)
        val accuracyTextView: TextView = itemView.findViewById(R.id.accuracyTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stats_item, parent, false)
        return StatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val game = games[position]

        holder.gameTimeTextView.text = "Время игры: ${game.gameTime}"
        holder.totalClicksTextView.text = "Нажатия: ${game.totalClicks}"
        holder.mouseClicksTextView.text = "Попадания: ${game.mouseClicks}"
        holder.accuracyTextView.text = "Точность: ${game.accuracy}"
    }

    override fun getItemCount(): Int {
        return games.size
    }
}