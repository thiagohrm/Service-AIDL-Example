// IMyAidl.aidl
package com.android.car.servicestestapp;

import com.android.car.servicestestapp.IMyAidlCallback;

interface IMyAidl {
    void startTimer();
    void registerCallback(IMyAidlCallback cb);
    void removeCallback(IMyAidlCallback cb);
}