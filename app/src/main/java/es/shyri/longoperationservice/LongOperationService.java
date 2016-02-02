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

    public enum STATUS{
        IDLE,
        STARTING,
        RUNNING,
        CANCELLING
    }

    STATUS currentStatus =  STATUS.IDLE;

    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void performLongOperation() {
        currentStatus = STATUS.STARTING;
        updateStatus();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                int percentage = 0;

                //////////////////////// Remain in Starting status for a while
                while(percentage < 100) {
                    i++;
                    if(i == 20000000) {
                        percentage++;
                        i = 0;
                    }
                }
                ////////////////////////

                i = 0;
                percentage = 0;

                currentStatus = STATUS.RUNNING;
                updateStatus();

                //////////////////////// Start long operation
                while(percentage < 100) {
                    i++;
                    if(i == 30000000) {
                        percentage++;
                        updateProgress(percentage);
                        i = 0;
                    }
                }
                ////////////////////////


            }
        }).start();
    }

    private void updateStatus() {
        switch(currentStatus) {
            case STARTING: {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_message_starting))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true);
                startForeground(NOTIFICATION_ID, notificationBuilder.build());
                break;
            }
            case RUNNING: {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_message_running))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true)
                        .setContentInfo("0%");
                startForeground(NOTIFICATION_ID, notificationBuilder.build());
                break;
            }
            default:
                break;
        }
    }

    private void updateProgress(int progress) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_message_running))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setProgress(100, Integer.valueOf(progress), false)
                .setContentInfo(progress + "%");
        startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void cancelLongOperation() {

    }

    public STATUS getCurrentStatus() {
        return currentStatus;
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
