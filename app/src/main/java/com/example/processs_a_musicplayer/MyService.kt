package com.example.processs_a_musicplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {
    private  val TAG = "MyService"

    var binder = object : IMusicPlayer.Stub() {


        override fun start() {
            Log.d(TAG, "start: music player")
        }

        override fun stop() {
            Log.d(TAG, "stop: music player")
        }

    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: binding succesfull")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: service unbounded")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Myservice destroyed")
    }

}