package com.gvozditskiy.ufamenu;

import android.app.Application;

/**
 * Created by Alexey on 13.01.2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }

}
