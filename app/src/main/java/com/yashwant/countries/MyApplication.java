package com.yashwant.countries;

import android.app.Application;

/**
 * Created by AQEEL on 3/18/2019.
 */

public class MyApplication extends Application {
    private static MyApplication sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static MyApplication getInstance() {
        return sInstance ;
    }

}
