// IMyAidlCallback.aidl
package com.android.car.servicestestapp;


interface IMyAidlCallback {
    void fromService();
    void SendTimerText(String string);
}