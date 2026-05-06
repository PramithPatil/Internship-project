package com.example.surya_shaktisolarmonitor.utils

import kotlin.random.Random

/**
 * Utility object for simulating solar energy generation based on weather conditions.
 *
 * In a real-world app, this would connect to actual solar panel sensors/inverters.
 * Here we simulate realistic generation values based on weather patterns in India.
 */
object SimulationUtils {

    /** Default electricity rate in ₹ per unit (kWh) */
    const val DEFAULT_RATE_PER_UNIT = 8.0

    /**
     * Simulate daily solar generation based on weather conditions.
     *
     * Typical rooftop solar panel (3-5 kW) generation:
     * - Sunny: 12-18 kWh/day
     * - Cloudy: 5-10 kWh/day
     * - Rainy: 1-4 kWh/day
     *
     * @param weather Weather condition: "Sunny", "Cloudy", or "Rainy"
     * @return Simulated generation value in kWh
     */
    fun simulateGeneration(weather: String): Double {
        return when (weather.lowercase()) {
            "sunny" -> 12.0 + Random.nextDouble(6.0)    // 12-18 kWh
            "cloudy" -> 5.0 + Random.nextDouble(5.0)     // 5-10 kWh
            "rainy" -> 1.0 + Random.nextDouble(3.0)      // 1-4 kWh
            else -> 8.0 + Random.nextDouble(4.0)          // Default: 8-12 kWh
        }
    }

    /**
     * Calculate savings based on solar generation.
     *
     * @param solarUnits Energy generated in kWh
     * @param ratePerUnit Electricity rate in ₹ per kWh (default: ₹8)
     * @return Savings amount in ₹
     */
    fun calculateSavings(solarUnits: Double, ratePerUnit: Double = DEFAULT_RATE_PER_UNIT): Double {
        return if (solarUnits > 0) solarUnits * ratePerUnit else 0.0
    }

    /**
     * Calculate net energy usage.
     *
     * @param consumption Total energy consumed in kWh
     * @param generation Total solar energy generated in kWh
     * @return Net usage (positive = drawing from grid, negative = exporting to grid)
     */
    fun calculateNetUsage(consumption: Double, generation: Double): Double {
        return consumption - generation
    }

    /**
     * Calculate energy exported to the grid.
     * Only applicable when generation exceeds consumption.
     *
     * @param consumption Total energy consumed in kWh
     * @param generation Total solar energy generated in kWh
     * @return Exported energy in kWh (0 if consumption >= generation)
     */
    fun calculateExportedEnergy(consumption: Double, generation: Double): Double {
        return if (generation > consumption) generation - consumption else 0.0
    }

    /**
     * Calculate Green Energy Independence Score.
     * Represents the percentage of energy needs met by solar power.
     *
     * @param generation Total solar energy generated
     * @param consumption Total energy consumed
     * @return Independence percentage (0-100)
     */
    fun calculateIndependenceScore(generation: Double, consumption: Double): Double {
        if (consumption <= 0) return if (generation > 0) 100.0 else 0.0
        val score = (generation / consumption) * 100.0
        return score.coerceIn(0.0, 100.0)
    }

    /**
     * Simulate a battery level based on current generation.
     * Higher generation = higher battery level.
     *
     * @param generationKwh Current daily generation in kWh
     * @return Battery percentage (0-100)
     */
    fun simulateBatteryLevel(generationKwh: Double): Int {
        // Assume a 10 kWh battery capacity for household
        val batteryCapacity = 10.0
        val level = ((generationKwh / batteryCapacity) * 100).toInt()
        return level.coerceIn(0, 100)
    }

    /**
     * Get a weather emoji for display purposes.
     */
    fun getWeatherEmoji(weather: String): String {
        return when (weather.lowercase()) {
            "sunny" -> "☀\uFE0F"
            "cloudy" -> "⛅"
            "rainy" -> "\uD83C\uDF27\uFE0F"
            else -> "🌤\uFE0F"
        }
    }

    /**
     * Determine if conditions are ideal for running heavy appliances.
     * Returns true when generation is high (sunny weather, good output).
     */
    fun isIdealForHeavyAppliances(generationKwh: Double): Boolean {
        return generationKwh >= 12.0
    }
}
