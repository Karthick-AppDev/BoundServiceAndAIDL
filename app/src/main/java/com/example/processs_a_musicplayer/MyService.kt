package com.example.processs_a_musicplayer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class MyService : Service() {

    private val TAG = "MyService"
    var isStart = false
    val callbacks = RemoteCallbackList<IClientCallback>()

    private val airplaneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                val isAirplaneModeOn = intent.getBooleanExtra("state", false)
               // Toast.makeText(context, "Airplane Mode: $isAirplaneModeOn", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onReceive: airplane mode broad cast received in service")
                sendMessageToClient("Airplane Mode: $isAirplaneModeOn")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, filter)
    }

    var binder = object : IMusicPlayer.Stub() {

        override fun start() {
            Log.d(TAG, "start: music player")
            isStart = true
            sendMessageToClient("stop event triggered")
            CoroutineScope(Dispatchers.IO).launch {
                while (isStart) {
                    Log.d(TAG, "Music player is playing")
                    delay(1000)
                }
            }
        }

        override fun stop() {
            Log.d(TAG, "stop: music player")
            sendMessageToClient("stop event triggered")
            isStart = false
        }

        override fun getPlayerStatus(): Boolean {
            return isStart
        }

        override fun registerCallback(callback: IClientCallback?) {
            if (callback != null) {
                callbacks.register(callback)
            } else {
                Log.d(TAG, "registerCallback: callback is null")
            }
        }

        override fun unregisterCallback(callback: IClientCallback?) {
            if (callback != null) {
                callbacks.unregister(callback)
            } else {
                Log.d(TAG, "unregisterCallback: callback is null")
            }
        }
    }

    fun sendMessageToClient(msg: String?){
        for (i in 0 until callbacks.beginBroadcast()){
            callbacks.getBroadcastItem(i).onMessageReceived(msg ?: "triggered by service")
        }

        /*Need to finish broadcast once after in begins broadcast every time
        * Because once the broadcast begins it will be in broadcast state if we trigger
        * another broadcast app crashes because already in broadcast state*/

        callbacks.finishBroadcast()
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