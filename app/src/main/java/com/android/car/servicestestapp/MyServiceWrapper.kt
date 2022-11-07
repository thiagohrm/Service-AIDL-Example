package com.android.car.servicestestapp

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyServiceWrapper {

    private val TAG = "App.MyServiceWrapper"

    private lateinit var remoteService: IMyAidl
    private var _state = MutableStateFlow<Int>(0)
    var mState : StateFlow<Int> = _state
    private var _dateTime = MutableStateFlow("")
    var mDateTime : StateFlow<String> = _dateTime

    private val mCallback = object : IMyAidlCallback.Stub() {
        override fun fromService() {
            Log.i(TAG, "fromService()")
        }

        override fun SendTimerText(string: String?) {
            Log.i(TAG, "SendTimerText()")
            string?.let {
                _dateTime.value = it
            }
        }


    }

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected()")
            remoteService = IMyAidl.Stub.asInterface(service)
            try {
                remoteService.registerCallback(mCallback)
                _state.value = 1
            } catch (exception: RemoteException) {
                println(exception.message)
            }

            try {
                remoteService.fromActivity()
            } catch (exception: RemoteException) {
                println(exception.message)
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected()")
        }

    }

    fun bind(application: Application) {
        Log.i(TAG, "bind()")
        application.bindService(
            Intent(application, MyService::class.java),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun unbind() {
        Log.i(TAG, "unbind()")
    }

    fun getServiceText(): String {
        Log.i(TAG, "getServiceText()")
        return if (mState.value == 1) {
            remoteService.serviceText
        } else {
            ""
        }
    }

    fun startTimer(){
        Log.i(TAG,"startTimer()")
        if (mState.value == 1) {
            remoteService.startTimer()
        }
    }

}