// IMyAidl.aidl
package com.android.car.servicestestapp;

import com.android.car.servicestestapp.IMyAidlCallback;

interface IMyAidl {
    void fromActivity();
    void registerCallback(IMyAidlCallback cb);
    String getServiceText();
    void startTimer();
}