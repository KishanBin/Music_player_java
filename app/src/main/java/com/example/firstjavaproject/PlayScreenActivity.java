package com.example.firstjavaproject;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstjavaproject.models.AlbumArtUtil;
import com.example.firstjavaproject.models.Song;

import java.io.IOException;
import java.util.ArrayList;

public class PlayScreenActivity extends AppCompatActivity {

    String path;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable updateSeekbar;
    ArrayList<Song> songList;
    int position;

    ImageView img;
    TextView title;
    TextView artist;
    ImageButton playPauseBtn;
    SeekBar seekBar;
    ImageButton previous_btn;
    ImageButton next_btn;

    boolean[] isPlaying = {true};





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.play_screen);

        img = findViewById(R.id.playImage);
        title = findViewById(R.id.playTitle);
        artist = findViewById(R.id.playArtist);
        playPauseBtn = findViewById(R.id.btnPlay);
        seekBar = findViewById(R.id.seekbar);
        previous_btn = findViewById(R.id.prevButton);
        next_btn = findViewById(R.id.nextButton);


        String songTitle  = getIntent().getStringExtra("title");
        String songArtist  = getIntent().getStringExtra("artist");
                  path = getIntent().getStringExtra("path");
        String albumId = getIntent().getStringExtra("albumId");
        songList = (ArrayList<Song>) getIntent().getSerializableExtra("songList");
        position = getIntent().getIntExtra("position", 0);
        Bitmap albumArt = AlbumArtUtil.getAlbumArt(this, albumId);



        System.out.println("hello " + songList);



        // Set data
        title.setText(songTitle);
        artist.setText(songArtist);

        if (albumArt != null) {
            img.setImageBitmap(albumArt);
        } else {
            img.setImageResource(R.drawable.android_music_icon);
        }


        // Create MediaPlayer
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();   // Loads the song
            // Set max SeekBar length = song duration
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            updateSeekBar();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        playPauseBtn.setOnClickListener(v -> {
            if (isPlaying[0]) {
                playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
                mediaPlayer.pause();
                handler.removeCallbacks(updateSeekbar);
            } else {
                playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
                mediaPlayer.start();
                updateSeekBar();
            }

            isPlaying[0] = !isPlaying[0];   // update value
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        previous_btn.setOnClickListener(v -> playPreviousSong());

        next_btn.setOnClickListener(v -> playNextSong());
    }

    private void updateSeekBar() {

        seekBar.setMax(mediaPlayer.getDuration());

        updateSeekbar = new Runnable() {
            @Override
            public void run() {

                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                if (mediaPlayer.isPlaying()) {
                    handler.postDelayed(this, 300); // update every 0.3 sec
                }
            }
        };

        handler.postDelayed(updateSeekbar, 0);
    }

    private void playSong() {

        isPlaying[0] = true;
        mediaPlayer.reset();

        String path = songList.get(position).getPath();
        String titleText = songList.get(position).getSong_name();
        String artistText = songList.get(position).getArtist_name();
        String albumId = songList.get(position).getAlbumId();

        title.setText(titleText);
        artist.setText(artistText);

        Bitmap albumArt = AlbumArtUtil.getAlbumArt(this, albumId);
        if (albumArt != null) {
            img.setImageBitmap(albumArt);
        } else {
            img.setImageResource(R.drawable.android_music_icon);
        }

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

            seekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar();

            playPauseBtn.setImageResource(R.drawable.baseline_pause_24);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void playNextSong() {
        if (position < songList.size() - 1) {
            position++;
        } else {
            position = 0;  // loop to first song
        }

        playSong();
    }

    private void playPreviousSong() {
        if (position > 0) {
            position--;
        } else {
            position = songList.size() - 1; // go to last song
        }

        playSong();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}