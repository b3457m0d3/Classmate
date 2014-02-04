package com.classmateapp.mobile;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class MyApplication extends Application {

	/** Android application context */
    private static Context sContext = null;
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sContext = getApplicationContext();
        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    
    /**
     * Initializes any singleton classes
     */
    protected void initSingletons() {
        // Initialize App DDP State Singleton
        MyDDPState.initInstance(MyApplication.sContext);
    }
    
    /**
     * Gets application context
     * @return Android application context
     */
    public static Context getAppContext() {
        return MyApplication.sContext;
    }
	
}
