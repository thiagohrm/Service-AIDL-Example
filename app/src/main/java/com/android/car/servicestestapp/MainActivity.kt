package com.android.car.servicestestapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val TAG = "MyApp.MainActivity"
    private val service = MyServiceWrapper()
    private lateinit var textView: TextView
    private lateinit var button: Button

    override fun onStart() {
        Log.i(TAG, "onStart()")
        super.onStart()
        service.bind(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView.visibility = View.GONE

        button = findViewById(R.id.button)
        button.setOnClickListener {
            clickToStartTimer()
        }
        button.isClickable = false

        service.state.observe(
            this,
            Observer {
                button.isClickable = it
            }
        )
        service.dateTime.observe(
            this,
            Observer {
                it?.let {
                    textView.text = it
                }
            }
        )
        service.timerState.observe(
            this,
            Observer {
                when (it) {
                    true -> {
                        textView.visibility = View.VISIBLE
                        button.isClickable = false
                        button.visibility = View.INVISIBLE

                    }
                    false -> {
                        textView.visibility = View.INVISIBLE
                        button.isClickable = true
                        button.visibility = View.VISIBLE
                    }
                }
            }
        )


    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        super.onDestroy()
        service.unbind(application)
    }


    private fun clickToStartTimer() {
        service.startTimer()
    }
}