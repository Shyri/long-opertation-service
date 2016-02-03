package es.shyri.longtaskservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    LongTaskService mService;
    boolean mBound;

    @Bind(R.id.textViewMessage)
    TextView textViewMessage;

    @Bind(R.id.textViewProgress)
    TextView textViewProgress;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.buttonStartLongTask)
    Button buttonStartLongTask;

    @Bind(R.id.buttonStopLongTask)
    Button buttonStopLongTask;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to the Service, cast the IBinder and get LocalService instance
            LongTaskService.LocalBinder binder = (LongTaskService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, LongTaskService.class), mConnection, Context.BIND_AUTO_CREATE);
        ButterKnife.bind(this);

        buttonStartLongTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LongTaskService.class);
                startService(intent);
                if(mBound) {
                    mService.performLongTask();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBound) unbindService(mConnection);
    }
}
