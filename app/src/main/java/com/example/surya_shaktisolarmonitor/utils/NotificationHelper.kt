package com.example.surya_shaktisolarmonitor.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * Helper class for managing solar energy notifications.
 *
 * Provides smart notifications to help users optimize their energy usage:
 * - High generation alerts (ideal for heavy appliances)
 * - Daily summary notifications
 */
object NotificationHelper {

    private const val CHANNEL_ID = "surya_shakti_solar_channel"
    private const val CHANNEL_NAME = "Solar Alerts"
    private const val CHANNEL_DESCRIPTION = "Smart notifications for solar energy monitoring"

    /**
     * Create the notification channel (required for Android 8.0+).
     * Should be called once during app initialization.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Send a notification when solar generation is high.
     * Alerts the user that it's an ideal time for heavy appliances.
     *
     * @param context Application context
     * @param generationKwh Current generation value in kWh
     */
    fun sendHighGenerationNotification(context: Context, generationKwh: Double) {
        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return // Permission not granted, skip notification
            }
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("☀\uFE0F High Sun Alert!")
            .setContentText(
                "Generation: ${String.format("%.1f", generationKwh)} kWh — " +
                        "Ideal time for heavy appliances like AC, washing machine, or water heater."
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Your solar panels are generating ${
                            String.format(
                                "%.1f",
                                generationKwh
                            )
                        } kWh right now!\n\n" +
                                "☀\uFE0F This is an ideal time to run heavy appliances:\n" +
                                "• Air Conditioner\n" +
                                "• Washing Machine\n" +
                                "• Water Heater\n" +
                                "• Electric Iron\n\n" +
                                "Make the most of free solar energy! \uD83C\uDF3F"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)
    }

    /**
     * Send a daily summary notification.
     *
     * @param context Application context
     * @param generationKwh Today's generation
     * @param savingsRupees Today's savings
     */
    fun sendDailySummaryNotification(
        context: Context,
        generationKwh: Double,
        savingsRupees: Double
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("\uD83C\uDF1E Daily Solar Summary")
            .setContentText(
                "Generated: ${String.format("%.1f", generationKwh)} kWh | " +
                        "Saved: ₹${String.format("%.0f", savingsRupees)}"
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1002, notification)
    }
}
