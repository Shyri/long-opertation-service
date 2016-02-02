package es.shyri.longoperationservice;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Shyri on 01/02/2016.
 */
public class LongOperationService extends Service {
    public static final int NOTIFICATION_ID = 1234;
    private NotificationManager nm;

    private final IBinder mBinder = new LocalBinder();

    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LongOperationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LongOperationService.this;
        }
    }
}
