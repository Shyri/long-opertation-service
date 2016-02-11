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
        END_CANCELLED
    }

    STATUS currentStatus =  STATUS.IDLE;
    boolean isCancelled = false;

    public LongTaskRunnable(LontTaskInterface listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        //////////////////////// Remain in Starting status for a while
        doStartingStage();
        ////////////////////////

        //////////////////////// Start long task
        doTaskStage();
        ///////////////////////
    }

    private void doStartingStage() {
        currentStatus = STATUS.STARTING;
        listener.onStatusUpdate(currentStatus);

        int i = 0;
        int percentage = 0;
        while(percentage < 100) {
            i++;
            if(i == 20000000) {
                percentage++;
                i = 0;
                if(isCancelled){
                    doCancellingStage();
                    return;
                }
            }
        }
    }

    private void doTaskStage() {
        currentStatus = STATUS.RUNNING;
        listener.onStatusUpdate(currentStatus);

        int i = 0;
        int percentage = 0;
        while(percentage < 100) {
            i++;
            if(i == 40000000) {
                percentage++;
                listener.onProgressUpdate(percentage);
                i = 0;
                if(isCancelled){
                    doCancellingStage();
                    return;
                }
            }
        }


        currentStatus = STATUS.END_SUCCESSFULLY;
        listener.onStatusUpdate(currentStatus);
    }

    private void doCancellingStage() {
        currentStatus = STATUS.CANCELLING;
        listener.onStatusUpdate(currentStatus);

        int i = 0;
        int percentage = 0;
        while(percentage < 100) {
            i++;
            if(i == 20000000) {
                percentage++;
                i = 0;
            }
        }

        currentStatus = STATUS.END_CANCELLED;
        listener.onStatusUpdate(currentStatus);
    }

    public STATUS getCurrentStatus() {
        return currentStatus;
    }

    public void cancel() {
        isCancelled = true;
    }

    public interface LontTaskInterface {
        void onStatusUpdate(STATUS newStatus);
        void onProgressUpdate(int percentage);
    }
}
