package com.android.car.servicestestapp

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import java.util.*


class MyService : Service() {

    private val TAG = "MyApp.MyService"

    private val callbacks: RemoteCallbackList<IMyAidlCallback> =
        RemoteCallbackList<IMyAidlCallback>()

    override fun onCreate() {
        Log.i(TAG, "onCreate()")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand()")
        return START_STICKY
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        super.onDestroy()
    }

    private val binder = object : IMyAidl.Stub() {
        override fun registerCallback(cb: IMyAidlCallback?) {
            Log.i(TAG, "registerCallback()")
            cb?.let {
                Log.i(TAG, "registering callback...")
                callbacks.register(cb)
            }
        }

        override fun startTimer() {
            Log.i(TAG, "startTimer()")
            startDateTime()
        }

        override fun removeCallback(cb: IMyAidlCallback?) {
            Log.i(TAG, "removeCallback()")
            callbacks.unregister(cb)
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.i(TAG, "onBind")
        return binder
    }

    private fun startDateTime() {
        Log.i(TAG, "startDateTime()")
        Handler(mainLooper).post {
            val timer = object : CountDownTimer(20000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i(TAG, "onTick()")
                    try {
                        val n: Int = callbacks.beginBroadcast()
                        Log.i(TAG, "beginBroadcast - $n times")
                        for (i in 0 until n) {
                            callbacks.getBroadcastItem(i)
                                .timerState(true)
                            callbacks.finishBroadcast()
                        }
                    } catch (exception: RemoteException) {
                        println(exception.message)
                    }
                    try {
                        val n: Int = callbacks.beginBroadcast()
                        Log.i(TAG, "beginBroadcast - $n times")
                        for (i in 0 until n) {
                            callbacks.getBroadcastItem(i)
                                .sendTimerText(Calendar.getInstance().time.toString())
                            callbacks.finishBroadcast()
                        }
                    } catch (exception: RemoteException) {
                        println(exception.message)
                    }
                }

                override fun onFinish() {
                    Log.i(TAG, "onFinish()")
                    try {
                        val n: Int = callbacks.beginBroadcast()
                        Log.i(TAG, "beginBroadcast - $n times")
                        for (i in 0 until n) {
                            callbacks.getBroadcastItem(i)
                                .timerState(false)
                            callbacks.finishBroadcast()
                        }
                    } catch (exception: RemoteException) {
                        println(exception.message)
                    }
                }
            }
            timer.start()
        }
    }
}