package com.android.car.servicestestapp

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import java.util.*


class MyService : Service() {

    private val TAG = "App.MyService"

    private val text = "Hello World Service"

    private val mCallbacks: RemoteCallbackList<IMyAidlCallback> =
        RemoteCallbackList<IMyAidlCallback>()

    private val mBinder = object : IMyAidl.Stub() {
        override fun fromActivity() {
            Log.i(TAG, "fromActivity()")
            fromActivityProcess()
        }

        override fun registerCallback(cb: IMyAidlCallback?) {
            Log.i(TAG, "registerCallback()")
            cb?.let {
                Log.i(TAG, "registering callback...")
                mCallbacks.register(cb)
            }
        }

        override fun getServiceText(): String {
            return text
        }

        override fun startTimer() {
            Log.i(TAG,"startTimer()")
            startDateTime()
        }

    }

    override fun onBind(p0: Intent?): IBinder {
        Log.i(TAG, "onBind")
        return mBinder
    }

    private fun fromActivityProcess() {
        Log.i(TAG, "fromActivityProcess")
        try {
        val n : Int = mCallbacks.beginBroadcast()
            Log.i(TAG,"beginBroadcast - $n times")
            mCallbacks.getBroadcastItem(0).fromService()
            mCallbacks.finishBroadcast()
        } catch (exception: RemoteException) {
            println(exception.message)
        }
    }
    private fun startDateTime(){
        Log.i(TAG,"startDateTime()")
        val timer = object : CountDownTimer(20000,1000){
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG,"onTick()")
                try {
                    val n : Int = mCallbacks.beginBroadcast()
                    Log.i(TAG,"beginBroadcast - $n times")
                    mCallbacks.getBroadcastItem(0)
                        .SendTimerText(Calendar.getInstance().time.toString())
                    mCallbacks.finishBroadcast()
                } catch (exception: RemoteException) {
                    println(exception.message)
                }
            }

            override fun onFinish() {
                Log.i(TAG,"onFinish()")
            }
        }
        timer.start()
    }
}