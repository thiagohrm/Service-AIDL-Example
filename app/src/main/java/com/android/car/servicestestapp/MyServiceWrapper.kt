package com.android.car.servicestestapp

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyServiceWrapper {

    private val TAG = "MyApp.MyServiceWrapper"

    private var remoteService: IMyAidl? = null
    private var _state = MutableLiveData(false)
    var state : LiveData<Boolean> = _state
    private var _dateTime = MutableLiveData("")
    var dateTime : LiveData<String> = _dateTime
    private var _timerState = MutableLiveData(false)
    var timerState : LiveData<Boolean> = _timerState

    private val callback = object : IMyAidlCallback.Stub() {
        override fun sendTimerText(string: String?) {
            Log.i(TAG, "SendTimerText()")
            string?.let {
                _dateTime.postValue(it)
            }
        }

        override fun timerState(state: Boolean) {
            Log.i(TAG,"timerState($state)")
            if (_timerState.value != state){
                _timerState.postValue(state)
            }
        }


    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected()")
            remoteService = IMyAidl.Stub.asInterface(service)
            try {
                remoteService?.registerCallback(callback)
                _state.value = true
            } catch (exception: RemoteException) {
                println(exception.message)
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected()")
            try {
                remoteService?.removeCallback(callback)
                _state.value = false
            } catch (exception: RemoteException) {
                println(exception.message)
            }
            remoteService = null
        }

    }

    fun bind(application: Application) {
        Log.i(TAG, "bind()")
        application.bindService(
            Intent(application, MyService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun unbind(application: Application) {
        Log.i(TAG, "unbind()")
        try {
            remoteService?.removeCallback(callback)
            _state.value = false
        } catch (exception: RemoteException) {
            println(exception.message)
        }
        application.unbindService(serviceConnection)
    }

    fun startTimer(){
        Log.i(TAG,"startTimer()")
        if (state.value == true) {
            remoteService?.startTimer()
        }
    }

}