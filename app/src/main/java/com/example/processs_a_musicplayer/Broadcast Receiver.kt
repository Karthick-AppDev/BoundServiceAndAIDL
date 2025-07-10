package com.example.processs_a_musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log

class AirPlaneModeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = context?.let { isAirplaneModeOn(it) }

            if (isAirplaneModeOn == true) {
                // Airplane mode is enabled
                Log.d("AirplaneMode", "Flight mode turned ON")
            } else {
                // Airplane mode is disabled
                Log.d("AirplaneMode", "Flight mode turned OFF")
            }
        }
    }
    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }
}