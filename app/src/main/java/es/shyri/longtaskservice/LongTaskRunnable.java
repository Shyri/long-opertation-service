package es.shyri.longtaskservice;

/**
 * Created by Shyri on 08/02/2016.
 */
public class LongTaskRunnable implements Runnable {
    LontTaskInterface listener;
    public enum STATUS{
        IDLE,
        STARTING,
        RUNNING,
        CANCELLING,
        END_SUCCESSFULLY,
        END_ERROR,
        END_CANCELED
    }

    STATUS currentStatus =  STATUS.IDLE;

    public LongTaskRunnable(LontTaskInterface listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        currentStatus = STATUS.STARTING;
        listener.onStatusUpdate(currentStatus);

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
        listener.onStatusUpdate(currentStatus);

        //////////////////////// Start long task
        while(percentage < 100) {
            i++;
            if(i == 40000000) {
                percentage++;
                listener.onProgressUpdate(percentage);
                i = 0;
            }
        }
        ///////////////////////

        currentStatus = STATUS.END_SUCCESSFULLY;
        listener.onStatusUpdate(currentStatus);
    }

    public STATUS getCurrentStatus() {
        return currentStatus;
    }

    public interface LontTaskInterface {
        void onStatusUpdate(STATUS newStatus);
        void onProgressUpdate(int percentage);
    }
}
