package com.example.space.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.space.MainActivity
import com.example.space.R
import com.example.space.utils.CHANNEL_ID
import com.example.space.utils.NOTIFICATION_ID
import com.example.space.utils.PUSH_NOTIFICATION_ACTION_KEY
import com.example.space.utils.PUSH_NOTIFICATION_TO_MAP

class ChargingBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val intentMain = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentMain,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            } else {
                PendingIntent.FLAG_ONE_SHOT
            }
        )

        val actionIntent = Intent(context, MainActivity::class.java)
        actionIntent.putExtra(PUSH_NOTIFICATION_ACTION_KEY, PUSH_NOTIFICATION_TO_MAP)
        val actionPendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                actionIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.charge_notification_body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .addAction(
                R.drawable.app_icon,
                context.getString(R.string.notification_action),
                actionPendingIntent
            )
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}
