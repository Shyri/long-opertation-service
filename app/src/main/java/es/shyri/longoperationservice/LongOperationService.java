package es.shyri.longoperationservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Shyri on 01/02/2016.
 */
public class LongOperationService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
