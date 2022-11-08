package com.android.car.servicestestapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    private val TAG = "MyApp.BootReceiver"
    private val actionStartService = "android.intent.action.BOOT_COMPLETED"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "onReceive")
        intent?.let {
            if (it.action == actionStartService) {
                val pendingIntent = Intent(
                    context,
                    MyService::class.java
                )
                Log.i(TAG,"starting service")
                context?.startService(pendingIntent)
            }
        }

    }
}