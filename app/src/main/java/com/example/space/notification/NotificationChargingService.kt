package com.example.space.notification

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.space.MainActivity
import com.example.space.R
import com.example.space.utils.*

class NotificationChargingService: Service() {

    var isServiceRunning: Boolean = false

    private val broadcastReceiver = ChargingBroadcastReceiver {
        createAndSendNotification()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        isServiceRunning = true
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startDesiresForeground()
        } else {
            startForeground(1, Notification())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        val broadcastIntent = Intent().apply {
            action = INTENT_ACTION
            setClass(this@NotificationChargingService, RestartServiceBroadcastReceiver::class.java)
        }
        unregisterReceiver(broadcastReceiver)
        sendBroadcast(broadcastIntent)
        isServiceRunning = false
    }

    private fun startDesiresForeground() {
        IntentFilter(Intent.ACTION_POWER_CONNECTED).apply {
            registerReceiver(broadcastReceiver, this)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_info_title2)))
            .setContentText(getString(R.string.notification_info_title2))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    private fun createAndSendNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            } else {
                PendingIntent.FLAG_ONE_SHOT
            }
        )

        val actionIntent = Intent(this, MainActivity::class.java)
        actionIntent.putExtra(PUSH_NOTIFICATION_ACTION_KEY, PUSH_NOTIFICATION_TO_MAP)
        val actionPendingIntent =
            PendingIntent.getActivity(
                this,
                1,
                actionIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.charge_notification_body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .addAction(
                R.drawable.app_icon,
                getString(R.string.notification_action),
                actionPendingIntent
            )
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}