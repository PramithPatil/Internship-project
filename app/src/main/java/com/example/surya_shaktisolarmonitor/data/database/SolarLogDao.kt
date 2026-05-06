package com.example.surya_shaktisolarmonitor.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for SolarLog entity.
 *
 * Provides all database operations for solar generation/consumption logs.
 * Uses Flow for reactive data observation in the UI layer.
 */
@Dao
interface SolarLogDao {

    /** Insert a new solar log entry. Replaces on conflict (same primary key). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SolarLog)

    /** Get all logs ordered by date descending (most recent first). */
    @Query("SELECT * FROM solar_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<SolarLog>>

    /** Get logs from the last 30 days for report generation. */
    @Query("SELECT * FROM solar_logs ORDER BY date DESC LIMIT 30")
    fun getLast30DaysLogs(): Flow<List<SolarLog>>

    /** Get the total solar energy generated across all records. */
    @Query("SELECT COALESCE(SUM(generationKwh), 0.0) FROM solar_logs")
    fun getTotalGeneration(): Flow<Double>

    /** Get the total energy consumed across all records. */
    @Query("SELECT COALESCE(SUM(consumptionKwh), 0.0) FROM solar_logs")
    fun getTotalConsumption(): Flow<Double>

    /** Get the total savings in ₹ across all records. */
    @Query("SELECT COALESCE(SUM(savingsRupees), 0.0) FROM solar_logs")
    fun getTotalSavings(): Flow<Double>

    /** Get the total energy exported to grid across all records. */
    @Query("SELECT COALESCE(SUM(exportedKwh), 0.0) FROM solar_logs")
    fun getTotalExported(): Flow<Double>

    /** Get total savings for the last 30 days (monthly savings). */
    @Query("SELECT COALESCE(SUM(savingsRupees), 0.0) FROM solar_logs ORDER BY date DESC LIMIT 30")
    fun getMonthlySavings(): Flow<Double>

    /** Get the most recent log entry (for dashboard display). */
    @Query("SELECT * FROM solar_logs ORDER BY date DESC LIMIT 1")
    fun getLatestLog(): Flow<SolarLog?>

    /** Delete all log entries (for testing/reset). */
    @Query("DELETE FROM solar_logs")
    suspend fun deleteAllLogs()
}
