// IMyAidlCallback.aidl
package com.android.car.servicestestapp;


interface IMyAidlCallback {
    void SendTimerText(String string);
    void timerState(boolean state);
}