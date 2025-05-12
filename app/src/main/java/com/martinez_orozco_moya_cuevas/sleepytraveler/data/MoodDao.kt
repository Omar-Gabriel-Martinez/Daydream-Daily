package com.martinez_orozco_moya_cuevas.sleepytraveler.data

import androidx.room.*

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries")
    fun getAll(): List<MoodEntry>

    @Query("SELECT * FROM mood_entries WHERE date = :date")
    fun getByDate(date: String): MoodEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: MoodEntry)
}
