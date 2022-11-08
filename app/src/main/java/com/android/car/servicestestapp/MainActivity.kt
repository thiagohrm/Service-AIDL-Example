package com.android.car.servicestestapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "MyApp.MainActivity"
    private val mService = MyServiceWrapper()

    override fun onStart() {
        Log.i(TAG, "onStart()")
        super.onStart()
        mService.bind(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            mService.mState.collect {
                Log.i(TAG, "mState - $it")
                if (it) {
                    serviceConnected()
                }
            }
        }

    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        super.onDestroy()
        mService.unbind(application)
    }

    private suspend fun serviceConnected() {
        Log.i(TAG, "serviceConnected()")
        this@MainActivity.runOnUiThread {
            Toast.makeText(application, mService.getServiceText(), Toast.LENGTH_LONG)
                .show()
        }
        this@MainActivity.runOnUiThread {
            mService.startTimer()
        }
        mService.mDateTime.collect {
            this@MainActivity.runOnUiThread {
                Log.i(TAG, "mDateTime collect - $it")
                val textView = findViewById<TextView>(R.id.textView)
                textView.text = it
            }
        }
    }
}