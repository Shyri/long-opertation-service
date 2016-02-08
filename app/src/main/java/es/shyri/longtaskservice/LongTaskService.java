package es.shyri.longtaskservice;

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
public class LongTaskService extends Service implements LongTaskRunnable.LontTaskInterface {
    public static final int NOTIFICATION_ID_PROGRESS = 1234;
    public static final int NOTIFICATION_ID_ENDED = 1235;
    private NotificationManager nm;
    private LongTaskRunnable longTaskRunnable;

    private final IBinder mBinder = new LocalBinder();

    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
        longTaskRunnable = new LongTaskRunnable(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void performLongTask() {
        new Thread(longTaskRunnable).start();
    }

    public void cancelLongTask() {

    }

    public LongTaskRunnable.STATUS getCurrentStatus() {
        return longTaskRunnable.getCurrentStatus();
    }

    public void setMessageHandler(Handler longTaskMessageHandler) {
        this.longTaskMessageHandler = longTaskMessageHandler;
    }

    @Override
    public void onStatusUpdate(LongTaskRunnable.STATUS currentStatus) {
        switch(currentStatus) {
            case STARTING: {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_message_starting))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true);
                startForeground(NOTIFICATION_ID_PROGRESS, notificationBuilder.build());
                break;
            }
            case RUNNING: {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_message_running))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true)
                        .setContentInfo("0%");
                startForeground(NOTIFICATION_ID_PROGRESS, notificationBuilder.build());
                break;
            }
            case END_SUCCESSFULLY: {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_message_end_successfully))
                        .setOngoing(false)
                        .setSmallIcon(R.mipmap.ic_launcher);
                nm.notify(NOTIFICATION_ID_ENDED, notificationBuilder.build());
                stopForeground(true);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_message_running))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setProgress(100, percentage, false)
                .setContentInfo(percentage + "%");
        startForeground(NOTIFICATION_ID_PROGRESS, notificationBuilder.build());
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LongTaskService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LongTaskService.this;
        }
    }
}
