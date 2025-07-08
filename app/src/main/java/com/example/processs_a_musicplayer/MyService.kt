package com.example.processs_a_musicplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : Service() {

    private  val TAG = "MyService"
    var  isStart = false

    var binder = object : IMusicPlayer.Stub() {

        override fun start() {
            Log.d(TAG, "start: music player")
            isStart = true
            CoroutineScope(Dispatchers.IO).launch {
                while (isStart){
                    Log.d(TAG, "Music player is playing")
                    delay(1000)
                }
            }

        }

        override fun stop() {
            Log.d(TAG, "stop: music player")
            isStart = false
        }

        override fun getPlayerStatus(): Boolean {
            return isStart
        }


    }


    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: binding successful")


        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: service unbounded")
        isStart = false

        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Myservice destroyed")
    }

}