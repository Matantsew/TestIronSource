package com.example.testironsource

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.app.NotificationCompat

class ActionNotificationService(private val context: Context) {

    private lateinit var notificationManager: NotificationManager

    fun showNotification(){

        val contactsIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        contactsIntent.setDataAndType(null, ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            contactsIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, ActionNotificationBroadcastReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_bathtub_24)
            .setContentTitle("Action")
            .setContentText(context.getString(R.string.action_is_notification))
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.ic_baseline_bathtub_24,
                "Action",
                incrementIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }

    fun obtainNotificationManager(){
        this.notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object{
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}