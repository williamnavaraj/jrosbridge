package net.xxhong.rosclient;

import android.app.Application;

import com.android.hp.ros.rosbridge.ROSBridgeClient;
//import android.content.Context;

//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;

/**
 * Created by xxhong on 16-11-21.
 */
public class RCApplication extends Application {
    private ROSBridgeClient client;
//    private RefWatcher refWatcher;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
//    }

    @Override
    public void onTerminate() {
        if (client != null)
            client.disconnect();
        super.onTerminate();
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        RCApplication application = (RCApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

    public ROSBridgeClient getRosClient() {
        return client;
    }

    public void setRosClient(ROSBridgeClient client) {
        this.client = client;
    }
}
