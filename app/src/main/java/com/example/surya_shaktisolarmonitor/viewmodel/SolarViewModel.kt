package com.example.surya_shaktisolarmonitor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.surya_shaktisolarmonitor.data.database.SolarDatabase
import com.example.surya_shaktisolarmonitor.data.database.SolarLog
import com.example.surya_shaktisolarmonitor.data.repository.SolarRepository
import com.example.surya_shaktisolarmonitor.utils.NotificationHelper
import com.example.surya_shaktisolarmonitor.utils.SimulationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ViewModel for the Surya-Shakti Solar Monitor app.
 *
 * Manages all UI state and business logic using StateFlow for reactive updates.
 * Acts as the bridge between the Repository (data layer) and UI (Compose screens).
 */
class SolarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SolarRepository

    // ──────────────────────────────────────────────
    // Database-backed reactive flows
    // ──────────────────────────────────────────────

    /** All solar logs from the database */
    val allLogs: StateFlow<List<SolarLog>>

    /** Last 30 days of logs for reports */
    val last30DaysLogs: StateFlow<List<SolarLog>>

    /** Aggregate statistics */
    val totalGeneration: StateFlow<Double>
    val totalConsumption: StateFlow<Double>
    val totalSavings: StateFlow<Double>
    val totalExported: StateFlow<Double>
    val monthlySavings: StateFlow<Double>
    val latestLog: StateFlow<SolarLog?>

    // ──────────────────────────────────────────────
    // UI State flows
    // ──────────────────────────────────────────────

    /** Currently selected weather condition for generation simulation */
    private val _selectedWeather = MutableStateFlow("Sunny")
    val selectedWeather: StateFlow<String> = _selectedWeather.asStateFlow()

    /** Simulated generation value based on selected weather */
    private val _simulatedGeneration = MutableStateFlow(0.0)
    val simulatedGeneration: StateFlow<Double> = _simulatedGeneration.asStateFlow()

    /** Current consumption input */
    private val _currentConsumption = MutableStateFlow(0.0)
    val currentConsumption: StateFlow<Double> = _currentConsumption.asStateFlow()

    /** Previous meter reading */
    private val _previousReading = MutableStateFlow(0.0)
    val previousReading: StateFlow<Double> = _previousReading.asStateFlow()

    /** Current meter reading */
    private val _currentReading = MutableStateFlow(0.0)
    val currentReading: StateFlow<Double> = _currentReading.asStateFlow()

    /** Electricity rate per unit in ₹ */
    private val _ratePerUnit = MutableStateFlow(SimulationUtils.DEFAULT_RATE_PER_UNIT)
    val ratePerUnit: StateFlow<Double> = _ratePerUnit.asStateFlow()

    /** Battery level percentage */
    private val _batteryLevel = MutableStateFlow(0)
    val batteryLevel: StateFlow<Int> = _batteryLevel.asStateFlow()

    /** Green Energy Independence Score */
    private val _independenceScore = MutableStateFlow(0.0)
    val independenceScore: StateFlow<Double> = _independenceScore.asStateFlow()

    /** Status/feedback messages for UI */
    private val _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    init {
        // Initialize database and repository
        val database = SolarDatabase.getDatabase(application)
        val dao = database.solarLogDao()
        repository = SolarRepository(dao)

        // Create notification channel on app startup
        NotificationHelper.createNotificationChannel(application)

        // Convert repository flows to StateFlows with initial values
        allLogs = repository.allLogs.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        last30DaysLogs = repository.last30DaysLogs.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        totalGeneration = repository.totalGeneration.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )
        totalConsumption = repository.totalConsumption.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )
        totalSavings = repository.totalSavings.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )
        totalExported = repository.totalExported.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )
        monthlySavings = repository.monthlySavings.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )
        latestLog = repository.latestLog.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), null
        )

        // Seed some sample data if the database is empty
        seedSampleDataIfEmpty()
    }

    // ──────────────────────────────────────────────
    // Generation Log Functions
    // ──────────────────────────────────────────────

    /** Update the selected weather condition and re-simulate generation. */
    fun updateWeather(weather: String) {
        _selectedWeather.value = weather
        _simulatedGeneration.value = SimulationUtils.simulateGeneration(weather)
        _batteryLevel.value = SimulationUtils.simulateBatteryLevel(_simulatedGeneration.value)
    }

    /** Simulate generation based on current weather. */
    fun simulateGeneration() {
        val gen = SimulationUtils.simulateGeneration(_selectedWeather.value)
        _simulatedGeneration.value = gen
        _batteryLevel.value = SimulationUtils.simulateBatteryLevel(gen)
    }

    /**
     * Log a generation entry with the current simulated values.
     * Creates a complete SolarLog record and saves it to the database.
     */
    fun logGeneration(manualGenerationKwh: Double? = null) {
        val generation = manualGenerationKwh ?: _simulatedGeneration.value

        if (generation <= 0) {
            _statusMessage.value = "⚠️ Please simulate or enter a valid generation value first."
            return
        }

        val consumption = _currentConsumption.value.coerceAtLeast(0.0)
        val netUsage = SimulationUtils.calculateNetUsage(consumption, generation)
        val exported = SimulationUtils.calculateExportedEnergy(consumption, generation)
        val savings = SimulationUtils.calculateSavings(generation, _ratePerUnit.value)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())

        val log = SolarLog(
            date = today,
            generationKwh = generation,
            consumptionKwh = consumption,
            weather = _selectedWeather.value,
            savingsRupees = savings,
            exportedKwh = exported,
            netUsageKwh = netUsage
        )

        viewModelScope.launch {
            repository.insertLog(log)
            _statusMessage.value = "✅ Generation logged: ${String.format("%.1f", generation)} kWh"

            // Update independence score
            val totalGen = totalGeneration.value + generation
            val totalCon = totalConsumption.value + consumption
            _independenceScore.value =
                SimulationUtils.calculateIndependenceScore(totalGen, totalCon)

            // Send notification if generation is high
            if (SimulationUtils.isIdealForHeavyAppliances(generation)) {
                NotificationHelper.sendHighGenerationNotification(
                    getApplication(),
                    generation
                )
            }
        }
    }

    // ──────────────────────────────────────────────
    // Consumption Tracker Functions
    // ──────────────────────────────────────────────

    /** Update previous meter reading. */
    fun updatePreviousReading(reading: Double) {
        _previousReading.value = reading.coerceAtLeast(0.0)
        calculateConsumption()
    }

    /** Update current meter reading. */
    fun updateCurrentReading(reading: Double) {
        _currentReading.value = reading.coerceAtLeast(0.0)
        calculateConsumption()
    }

    /** Calculate consumption from meter readings. */
    private fun calculateConsumption() {
        val diff = _currentReading.value - _previousReading.value
        _currentConsumption.value = diff.coerceAtLeast(0.0)
    }

    /** Directly set consumption value. */
    fun updateConsumption(consumption: Double) {
        _currentConsumption.value = consumption.coerceAtLeast(0.0)
    }

    /**
     * Log a consumption entry.
     * Uses current consumption and latest generation data.
     */
    fun logConsumption() {
        val consumption = _currentConsumption.value
        if (consumption <= 0) {
            _statusMessage.value = "⚠️ Please enter valid meter readings first."
            return
        }

        val generation = _simulatedGeneration.value
        val netUsage = SimulationUtils.calculateNetUsage(consumption, generation)
        val exported = SimulationUtils.calculateExportedEnergy(consumption, generation)
        val savings = SimulationUtils.calculateSavings(generation, _ratePerUnit.value)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Date())

        val log = SolarLog(
            date = today,
            generationKwh = generation,
            consumptionKwh = consumption,
            weather = _selectedWeather.value,
            savingsRupees = savings,
            exportedKwh = exported,
            netUsageKwh = netUsage
        )

        viewModelScope.launch {
            repository.insertLog(log)
            _statusMessage.value = "✅ Consumption logged: ${String.format("%.1f", consumption)} kWh"
        }
    }

    // ──────────────────────────────────────────────
    // Savings Calculator Functions
    // ──────────────────────────────────────────────

    /** Update the per-unit electricity rate. */
    fun updateRatePerUnit(rate: Double) {
        _ratePerUnit.value = rate.coerceAtLeast(0.0)
    }

    /** Clear status message after it's been shown. */
    fun clearStatusMessage() {
        _statusMessage.value = ""
    }

    // ──────────────────────────────────────────────
    // Sample Data Seeding
    // ──────────────────────────────────────────────

    /**
     * Seeds the database with sample data for the last 15 days.
     * Only runs if the database is empty (first launch).
     */
    private fun seedSampleDataIfEmpty() {
        viewModelScope.launch {
            // Collect the first emission to check if data exists
            repository.allLogs.collect { logs ->
                if (logs.isEmpty()) {
                    insertSampleData()
                }
                return@collect // Only check once
            }
        }
    }

    private suspend fun insertSampleData() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val weatherOptions = listOf("Sunny", "Sunny", "Cloudy", "Sunny", "Rainy",
            "Sunny", "Cloudy", "Sunny", "Sunny", "Rainy",
            "Cloudy", "Sunny", "Sunny", "Cloudy", "Sunny")

        for (i in 14 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val date = dateFormat.format(calendar.time)
            val weather = weatherOptions[14 - i]
            val generation = SimulationUtils.simulateGeneration(weather)
            val consumption = 8.0 + kotlin.random.Random.nextDouble(12.0) // 8-20 kWh typical household
            val savings = SimulationUtils.calculateSavings(generation)
            val netUsage = SimulationUtils.calculateNetUsage(consumption, generation)
            val exported = SimulationUtils.calculateExportedEnergy(consumption, generation)

            repository.insertLog(
                SolarLog(
                    date = date,
                    generationKwh = generation,
                    consumptionKwh = consumption,
                    weather = weather,
                    savingsRupees = savings,
                    exportedKwh = exported,
                    netUsageKwh = netUsage
                )
            )
        }

        // Set initial simulated values
        _simulatedGeneration.value = SimulationUtils.simulateGeneration("Sunny")
        _batteryLevel.value = SimulationUtils.simulateBatteryLevel(_simulatedGeneration.value)
        _independenceScore.value = 65.0 // Initial score
    }
}
