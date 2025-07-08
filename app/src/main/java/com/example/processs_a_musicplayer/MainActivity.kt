package com.example.processs_a_musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private  val TAG = "MainActivity"
    var iMusicPlayer : IMusicPlayer? = null
    var startButton : Button? = null
    var stopButton : Button? = null
    var statusTextView:TextView ? = null

    var serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iMusicPlayer = IMusicPlayer.Stub.asInterface(service)
            Log.d(TAG, "onServiceConnected: Connection successful on Process A")
       }

        override fun onServiceDisconnected(name: ComponentName?) {
            iMusicPlayer = null
            Log.d(TAG, "onServiceDisconnected: Connection disconnected on process B")
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onResume() {
        super.onResume()
        val intent = Intent("MyAidlService")
        intent.setPackage("com.example.processs_a_musicplayer")
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        Log.d(TAG, "onResume: Requested to bind the service")

        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        statusTextView = findViewById(R.id.statusTextView)

        statusTextView?.text = if (iMusicPlayer?.playerStatus == true) "Music player is playing..." else "Music player is stopped"

        startButton?.setOnClickListener {
            iMusicPlayer?.start()
            statusTextView?.text = if (iMusicPlayer?.playerStatus == true) "Music player is playing..." else "Music player is stopped"

        }
        stopButton?.setOnClickListener {
            iMusicPlayer?.stop()
            statusTextView?.text = if (iMusicPlayer?.playerStatus == true) "Music player is playing..." else "Music player is stopped"
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Service is unbounded from process A")
      //  unbindService(serviceConnection)
    }


}
