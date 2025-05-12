package com.martinez_orozco_moya_cuevas.sleepytraveler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MoodEntry::class], version = 1)
abstract class MoodDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile private var INSTANCE: MoodDatabase? = null

        fun getInstance(context: Context): MoodDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MoodDatabase::class.java,
                    "mood_db"
                ).build().also { INSTANCE = it }
            }
    }
}
