package com.android.car.servicestestapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log


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
}