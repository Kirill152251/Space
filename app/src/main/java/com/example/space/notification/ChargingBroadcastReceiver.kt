package com.example.space.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ChargingBroadcastReceiver(private val sendNotification: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotification()
    }
}