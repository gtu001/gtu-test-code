package com.example.gtuandroid.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VideoRecorder extends Activity {

    // Create objects of MediaRecorder and Preview class
    private MediaRecorder recorder;
    private Preview mPreview;

    boolean flag = false;
    boolean startedRecording = false;
    boolean stoppedRecording = false;

    // In this method, create an object of MediaRecorder class. Create an object
    // of
    // RecorderPreview class(Customized View). Add RecorderPreview class object
    // as content of UI.
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recorder = new MediaRecorder();
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mPreview = new Preview(VideoRecorder.this, recorder);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(mPreview);
    }

    /*
     * ! <p> Initialize the contents of the Activity's standard options menu.
     * Menu items are to be placed in to menu. This is called on each press of
     * menu button. In this options to start and stop recording are provided.
     * Option for start recording has group id 0 and option to stop recording is
     * 1. (first parameter of menu.add method). Start and stop have different
     * group id, if recording is already started then it shows stop option else
     * it shows start option. </p>
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        menu.add(0, 0, 0, "Start Recording");
        menu.add(1, 1, 0, "Stop Recording");

        menu.setGroupVisible(0, false);
        menu.setGroupVisible(1, false);

        if (startedRecording == false){
            menu.setGroupVisible(0, true);
        }else if (startedRecording == true && stoppedRecording == false){
            menu.setGroupVisible(1, true);
        }
        return true;
    }

    /*
     * ! <p> This method receives control when Item in menu option is selected.
     * It contains implementations to be performed on selection of menu item.
     * </p>
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0:
            // start the recorder
            recorder.start();
            startedRecording = true;

            break;

        case 1:
            // stop the recorder
            recorder.stop();
            recorder.release();
            recorder = null;
            stoppedRecording = true;
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    class Preview extends SurfaceView implements SurfaceHolder.Callback {
        // Create objects for MediaRecorder and SurfaceHolder.
        SurfaceHolder mHolder;
        MediaRecorder tempRecorder;

        // Create constructor of Preview Class. In this, get an object of
        // surfaceHolder class by calling getHolder() method. After that add
        // callback to the surfaceHolder. The callback will inform when surface
        // is
        // created/changed/destroyed. Also set surface not to have its own
        // buffers.
        public Preview(Context context, MediaRecorder recorder) {
            super(context);
            tempRecorder = recorder;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            // TODO Auto-generated constructor stub
        }

        public Surface getSurface() {
            return mHolder.getSurface();
        }

        // Implement the methods of SurfaceHolder.Callback interface

        // SurfaceCreated : This method gets called when surface is created.
        // In this, initialize all parameters of MediaRecorder object.
        // The output file will be stored in SD Card.

        public void surfaceCreated(SurfaceHolder holder) {
            tempRecorder.setOutputFile("/sdcard/recordvideooutput.3gpp");
            tempRecorder.setPreviewDisplay(mHolder.getSurface());
            try {
                tempRecorder.prepare();
            } catch (Exception e) {
                // String message = e.getMessage();
                tempRecorder.release();
                tempRecorder = null;
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if (tempRecorder != null) {
                tempRecorder.release();
                tempRecorder = null;
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        }
    }
}
