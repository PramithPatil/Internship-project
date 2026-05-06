package com.example.surya_shaktisolarmonitor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database class for Surya-Shakti Solar Monitor.
 *
 * Uses singleton pattern to ensure only one database instance exists.
 * Contains the SolarLog entity table for storing all solar data.
 */
@Database(entities = [SolarLog::class], version = 1, exportSchema = false)
abstract class SolarDatabase : RoomDatabase() {

    abstract fun solarLogDao(): SolarLogDao

    companion object {
        @Volatile
        private var INSTANCE: SolarDatabase? = null

        /**
         * Get the singleton database instance.
         * Uses double-checked locking for thread safety.
         */
        fun getDatabase(context: Context): SolarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SolarDatabase::class.java,
                    "surya_shakti_solar_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
