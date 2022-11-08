// IMyAidlCallback.aidl
package com.android.car.servicestestapp;


interface IMyAidlCallback {
    void sendTimerText(String string);
    void timerState(boolean state);
}