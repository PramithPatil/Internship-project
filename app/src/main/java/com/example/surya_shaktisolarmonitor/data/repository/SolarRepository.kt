package com.example.surya_shaktisolarmonitor.data.repository

import com.example.surya_shaktisolarmonitor.data.database.SolarLog
import com.example.surya_shaktisolarmonitor.data.database.SolarLogDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository for solar data operations.
 *
 * Acts as a single source of truth for the ViewModel layer.
 * Abstracts the data source (Room database) from the business logic.
 * All database operations are exposed as Flow for reactive updates.
 */
class SolarRepository(private val solarLogDao: SolarLogDao) {

    /** All logs, ordered by date descending */
    val allLogs: Flow<List<SolarLog>> = solarLogDao.getAllLogs()

    /** Last 30 days of logs for reports */
    val last30DaysLogs: Flow<List<SolarLog>> = solarLogDao.getLast30DaysLogs()

    /** Aggregate statistics as reactive flows */
    val totalGeneration: Flow<Double> = solarLogDao.getTotalGeneration()
    val totalConsumption: Flow<Double> = solarLogDao.getTotalConsumption()
    val totalSavings: Flow<Double> = solarLogDao.getTotalSavings()
    val totalExported: Flow<Double> = solarLogDao.getTotalExported()
    val monthlySavings: Flow<Double> = solarLogDao.getMonthlySavings()
    val latestLog: Flow<SolarLog?> = solarLogDao.getLatestLog()

    /** Insert a new solar log entry */
    suspend fun insertLog(log: SolarLog) {
        solarLogDao.insertLog(log)
    }

    /** Clear all data (for testing/reset) */
    suspend fun deleteAllLogs() {
        solarLogDao.deleteAllLogs()
    }
}
