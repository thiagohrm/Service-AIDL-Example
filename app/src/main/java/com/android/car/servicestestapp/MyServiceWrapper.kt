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
    private var _state = MutableLiveData<Boolean>(false)
    var mState : LiveData<Boolean> = _state
    private var _dateTime = MutableLiveData("")
    var mDateTime : LiveData<String> = _dateTime
    private var _timerState = MutableLiveData(false)
    var timerState : LiveData<Boolean> = _timerState

    private val mCallback = object : IMyAidlCallback.Stub() {
        override fun SendTimerText(string: String?) {
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

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected()")
            remoteService = IMyAidl.Stub.asInterface(service)
            try {
                remoteService?.registerCallback(mCallback)
                _state.value = true
            } catch (exception: RemoteException) {
                println(exception.message)
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected()")
            try {
                remoteService?.removeCallback(mCallback)
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
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun unbind(application: Application) {
        Log.i(TAG, "unbind()")
        try {
            remoteService?.removeCallback(mCallback)
            _state.value = false
        } catch (exception: RemoteException) {
            println(exception.message)
        }
        application.unbindService(mServiceConnection)
    }

    fun startTimer(){
        Log.i(TAG,"startTimer()")
        if (mState.value == true) {
            remoteService?.startTimer()
        }
    }

}