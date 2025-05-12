package com.martinez_orozco_moya_cuevas.sleepytraveler.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey val date: String,          // "dd/MM/yyyy"
    val title: String,
    val emotion: String,
    val value: Int,
    val description: String
)
