package com.hiddendev.musicplayerv2.feature;


import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mediaPlayer;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn=(Button) findViewById(R.id.playBtn);
        elapsedTimeLabel=(TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel=(TextView) findViewById(R.id.remainingTimeLabel);

        //Media Player
        mediaPlayer = MediaPlayer.create(this,R.raw.bird);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f,0.5f);
        totalTime=mediaPlayer.getDuration();

        //Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);

        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    positionBar.setProgress(progress);
                }



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Volume Bar
        volumeBar = (SeekBar)findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum = progress/100f;
                mediaPlayer.setVolume(volumeNum,volumeNum);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Thread

        new Thread(new Runnable() {
            @Override
            public void run() {
                while ((mediaPlayer!=null)){
                    try{
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                    }catch (Exception e){

                    }
                }
            }
        }).start();


    }

    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);


            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };
    public String createTimeLabel(int time){
        String timeLabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;
        timeLabel = min + ":";
        if(sec<10) timeLabel +="0";
        timeLabel += sec;
        return timeLabel;
    }



    public void playBtnClick(View view){
        if(!mediaPlayer.isPlaying()){
            // stopping
            mediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.pause);

        }else {
            mediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }

    }
}
