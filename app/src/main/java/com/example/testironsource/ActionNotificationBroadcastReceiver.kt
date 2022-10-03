package com.example.testironsource

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class ActionNotificationBroadcastReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        val service = ActionNotificationService(context)
        service.obtainNotificationManager()
        service.showNotification()
    }
}