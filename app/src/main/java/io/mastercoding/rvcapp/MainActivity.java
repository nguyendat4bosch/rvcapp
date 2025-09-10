package io.mastercoding.rvcapp;

import static java.sql.Types.NULL;

import android.car.Car;
import android.car.Car.CarServiceLifecycleListener;
import android.car.evs.CarEvsBufferDescriptor;
import android.car.evs.CarEvsManager;
import android.car.evs.CarEvsManager.*;
import android.car.evs.CarEvsStatus;
import android.content.ComponentName;
import android.content.res.Resources;
import android.graphics.PixelFormat;
//import android.graphics.PorterDuff.Mode.SRC_IN;
import android.hardware.HardwareBuffer;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import androidx.activity.viewModels;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
//import kotlinx.coroutines.MainScope;
//import kotlinx.coroutines.flow.launchIn;
//import kotlinx.coroutines.flow.onEach;

import java.util.concurrent.Executor;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import kotlin.jvm.Synchronized;

public class MainActivity extends AppCompatActivity {

    final String TAG =  "MainActivity";
    final int MAX_FRAME_LAG  = 3;
    final int EXPECTED_FPS = 30;

    final double FRAME_DROPPED_TIMEOUT_MS = (1000L/EXPECTED_FPS) * 2;

    final double  STREAM_FREEZE_TIMEOUT_MS = 2000L;

    /**
     * GLES20CarEvsCameraPreviewRenderer::FrameProvider::returnFrame implementation.
     */
    @VisibleForTesting
    //TODO: Add view parameter //Compact
    public void returnFrame( HardwareBuffer frame, ArrayList<CarEvsBufferDescriptor> buffer, CarEvsManager manager) {
        CarEvsBufferDescriptor desc = new CarEvsBufferDescriptor(0, buffer.getFirst().getHardwareBuffer());
        manager.returnFrameBuffer(desc);
        Log.d("Checkforreturnframe", "returnFrame called");
        if (!buffer.isEmpty()) {
            // Do nothing - improvement later
        }
    }

    /**
     * GLES20CarEvsCameraPreviewRenderer::FrameProvider::getNewFrame implementation.
     */
//    @VisibleForTesting
    //TODO: Check for Synchorizing
    HardwareBuffer getNewFrame(ArrayList<CarEvsBufferDescriptor> buffer) {
        if (buffer.isEmpty()) {
            return null;
        }
        return buffer.getFirst().getHardwareBuffer();
    }

    /**
     * CarServiceLifecycleListener::onLifecycleChanged implementation.
     *
     * Upon connection to the Car service it creates a CarEvsManager and post a signal
     * to the handler using provided message.
     */
    @VisibleForTesting
    // TODO: Check for ViewModel for later
    void onLifecycleChanged(Car car, Boolean ready, Integer type, Executor executor) {
        if (!ready) {
            Log.d(TAG, "Disconnected from the Car Service");
        } else {
            Log.d(TAG, "Connected to the Car Service");
            Object manager = car.getCarManager(Car.CAR_EVS_SERVICE);
        }
    }

    /**
     * CarEvsStreamCallback::onStreamEvent implementation.
     *
     * If the stream is stopped we release all pending frames in our buffer.
     */
    @VisibleForTesting
    //TODO:
    public void onStreamEvent(Integer event, ArrayList<CarEvsBufferDescriptor> buffer, CarEvsManager manager) {

    }

    /**
     * CarEvsStreamCallback::onNewFrame implementation.
     *
     * If there's no attached view OR if the buffer is full
     *      We immediately release the buffer and return the frame
     * Else
     *      We add a new frame to our buffer.
     *
     * Then we request the view to render the frame.
     */
    @VisibleForTesting
    void onNewFrame(CarEvsBufferDescriptor frame, ArrayList<CarEvsBufferDescriptor> buffer, CarEvsManager manager) {

        if (buffer.size() >= MAX_FRAME_LAG) {
            frame.hashCode();
            manager.returnFrameBuffer(frame);
        } else {
            buffer.add(frame);
        }
    }

    @VisibleForTesting
    void requestStartStream(Integer type, CarEvsManager manager, IBinder token, ExecutorService callbackExecutor, CarEvsStreamCallback carEvsStreamCallback, Resources resources ) {
        //SERVICE_STATE_ACTIVE, SERVICE_STATE_INACTIVE, SERVICE_STATE_REQUESTED
        if(manager.getCurrentStatus().getState() == 3) {
//        SERVICE_STATE_UNAVAILABLE -> Log.w(TAG, "Service type $type is unavailable!");
            manager.startVideoStream(
                    type,
                    token,
                    callbackExecutor,
                    carEvsStreamCallback);
        } else {
           Log.w(TAG, "Service type $type is unavailable!");
        }

    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("CreateCheck", "onCreate called");

        // Intents: facilitates communication bet. different components of an app,
        //          as well as bet. different applications.

        // types of intents:
        // 1- Explicit Intents
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSecondActivity();
            }
        });

        // 2- Implicit Intents
        Button btn2 = findViewById(R.id.openBrowser);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEVSCameraHal();
            }
        });

        // 3- Setup for Toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d("MenuCheck", "onCreateOptionsMenu called");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("MenuCheck", "onOptionsItemSelected called");
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Handle settings action
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void goToSecondActivity(){
        Intent intent = new Intent(this, SecondActivity.class );
        startActivity(intent);
    }

    public void openEVSCameraHal() {
        Uri path = Uri.parse("");

//        Intent intent = new Intent(Intent.ACTION_VIEW, path);

        Intent intent = new Intent(this, Menu.class);

//        startActivity(intent);
    }

}