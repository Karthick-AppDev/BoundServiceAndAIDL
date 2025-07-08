package com.example.processs_a_musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var iMusicPlayer : IMusicPlayer? = null
    var mBound = false

    var serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iMusicPlayer = IMusicPlayer.Stub.asInterface(service)
            iMusicPlayer?.start()
            mBound = true
       }

        override fun onServiceDisconnected(name: ComponentName?) {
            iMusicPlayer = null
            mBound = false
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
        iMusicPlayer?.start()
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        mBound = false
    }


}
