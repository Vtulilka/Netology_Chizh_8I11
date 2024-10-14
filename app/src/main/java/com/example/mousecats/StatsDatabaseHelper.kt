package com.example.mousecats

import android.app.GameState
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StatsDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object {
            private const val DATABASE_NAME = "game_stats.db"
            private const val DATABASE_VERSION = 1

            const val TABLE_NAME = "game_stats"
            const val COLUMN_ID = "id"
            const val COLUMN_TOTAL_CLICKS = "total_clicks"
            const val COLUMN_MOUSE_CLICKS = "mouse_clicks"
            const val COLUMN_ACCURACY = "accuracy"
            const val COLUMN_GAME_TIME = "game_time"
        }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TOTAL_CLICKS INTEGER, " +
                "$COLUMN_MOUSE_CLICKS INTEGER, " +
                "$COLUMN_ACCURACY INTEGER, " +
                "$COLUMN_GAME_TIME TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addGame(totalClicks: Int, mouseClicks: Int, accuracy: Int, gameTime: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TOTAL_CLICKS, totalClicks)
            put(COLUMN_MOUSE_CLICKS, mouseClicks)
            put(COLUMN_ACCURACY, accuracy)
            put(COLUMN_GAME_TIME, gameTime)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getLastTenGames(): List<GameStats> {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC LIMIT 10"
        val cursor = db.rawQuery(query, null)
        val gameStatsList = mutableListOf<GameStats>()

        if (cursor.moveToFirst()) {
            do {
                val totalClicks = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CLICKS))
                val mouseClicks = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOUSE_CLICKS))
                val accuracy = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACCURACY))
                val gameTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAME_TIME))

                gameStatsList.add(GameStats(totalClicks, mouseClicks, accuracy, gameTime))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return gameStatsList
    }
}

data class GameStats(
    val totalClicks: Int,
    val mouseClicks: Int,
    val accuracy: Int,
    val gameTime: String
)