// IMusicPlayer.aidl
package com.example.processs_a_musicplayer;

import com.example.processs_a_musicplayer.IClientCallback;

// Declare any non-default types here with import statements

interface IMusicPlayer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */


    void start();

    void stop();

    boolean getPlayerStatus();

    /*Need to import mannually IClientCallback package*/

     void registerCallback(IClientCallback callback);
     void unregisterCallback(IClientCallback callback);
}