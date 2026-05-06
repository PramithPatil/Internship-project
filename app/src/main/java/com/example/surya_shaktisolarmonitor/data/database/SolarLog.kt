package com.example.surya_shaktisolarmonitor.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * SolarLog Entity - Stores daily solar generation and consumption records.
 *
 * Each entry represents one day's data including:
 * - Solar energy generated (kWh)
 * - Energy consumed (kWh)
 * - Weather condition at the time of logging
 * - Calculated savings based on per-unit electricity rate
 * - Any exported energy (generation exceeding consumption)
 */
@Entity(tableName = "solar_logs")
data class SolarLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Date string in format "yyyy-MM-dd" */
    val date: String,

    /** Solar energy generated in kWh */
    val generationKwh: Double,

    /** Energy consumed in kWh */
    val consumptionKwh: Double,

    /** Weather condition: "Sunny", "Cloudy", or "Rainy" */
    val weather: String,

    /** Savings in ₹ (generationKwh × perUnitRate) */
    val savingsRupees: Double,

    /** Energy exported to grid in kWh (when generation > consumption) */
    val exportedKwh: Double,

    /** Net usage in kWh (consumption - generation, can be negative) */
    val netUsageKwh: Double,

    /** Timestamp of when this entry was created */
    val timestamp: Long = System.currentTimeMillis()
)
