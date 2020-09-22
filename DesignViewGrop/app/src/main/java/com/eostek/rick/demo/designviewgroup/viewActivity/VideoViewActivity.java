package com.eostek.rick.demo.designviewgroup.viewActivity;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;

//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
import com.eostek.rick.demo.designviewgroup.R;

public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener {

    //private VideoView mVideoView;

    private Button mbutton;

    private ProgressBar mProgressBar;

    private String videoPathDir = Environment.getExternalStorageDirectory().getAbsolutePath();

    private String defaultPath = "/storage/emulated/0/download/test.mp4";

    private String pathVideoOne = null;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressBar.setVisibility(View.GONE);
            //mVideoView.setVideoPath(pathVideoOne);

            //mVideoView.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        //mVideoView = (VideoView) findViewById(R.id.video_view);
        mbutton = (Button) findViewById(R.id.start_play);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        //mVideoView.setMediaController(new MediaController(VideoViewActivity.this));

        new Thread(new Runnable() {
            @Override
            public void run() {
                //pathVideoOne = getVideoOnePath(initFiles(videoPathDir),videoPathDir);
                pathVideoOne = findCurrentDirFirstVideo(videoPathDir);
                Log.v("tag1", "pathVideoOne : " + pathVideoOne);
                //mhandler.sendEmptyMessage(1);
            }
        }).start();
        setListener();

    }

    private String findCurrentDirFirstVideo(String dirPath) {
        String tmp = null;
        if (dirPath == null) {
            return null;
        }
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return null;
        } else {
            String s[] = file.list();
            for (int i = 0; i < s.length; i++) {
                tmp = findCurrentDirFirstVideo(dirPath + "/" + s[i]);
                if (tmp != null) {
                    return tmp;
                }
            }
            for (int i = 0; i < s.length; i++) {

                if (s[i].contains("mp4")) {
                    return dirPath + "/" + s[i];
                }
            }
        }

        return null;
    }

    private void setListener() {
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pathVideoOne == null) {
                    pathVideoOne = defaultPath;
                }
                //mVideoView.setVideoPath(pathVideoOne);
                //mVideoView.start();
                //mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    //@Override
                    //public void onPrepared(MediaPlayer mp) {
                        //mProgressBar.setVisibility(View.GONE);
                    //}
                //});
            }
        });

        /*mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float downX = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float offsetX = event.getX() - downX;
                        Log.v("tag", "ACTION_MOVE downX : " + downX);
                        Log.v("tag", "ACTION_MOVE event.getX() : " + event.getX());
                        Log.v("tag", "ACTION_MOVE offset : " + offsetX);
                        if (Math.abs(offsetX) < 50) {
                            break;
                        }
                        float precent = offsetX / mVideoView.getWidth();
                        long seekTime = (long) (mVideoView.getDuration() * precent + mVideoView.getCurrentPosition());
                        mVideoView.seekTo(seekTime);
                        mVideoView.start();
                        break;
                }


                return true;
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress:
                break;
        }
    }
}
