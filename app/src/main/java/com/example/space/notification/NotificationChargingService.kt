package com.example.space.notification

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.space.R
import com.example.space.utils.*

class NotificationChargingService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startDesiresForeground()
        } else {
            startForeground(1, Notification())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun startDesiresForeground() {
        val broadcastReceiver = ChargingBroadcastReceiver()
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
}