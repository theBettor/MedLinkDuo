package com.bettor.medlinkduo.data.ble

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BleKeepAliveService : Service() {
    override fun onBind(p0: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        val chId = "ble"
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(NotificationChannel(chId, "BLE", NotificationManager.IMPORTANCE_LOW))
        }
        val notif =
            NotificationCompat.Builder(this, chId)
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .setContentTitle("Device connected")
                .setContentText("Maintaining connection…")
                .build()
        startForeground(1001, notif)
    }
}
