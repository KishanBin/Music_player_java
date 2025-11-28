package com.example.firstjavaproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.play_screen);

        //stick view with variables
        ImageView img = findViewById(R.id.playImage);
        TextView title = findViewById(R.id.playTitle);
        TextView artist = findViewById(R.id.playArtist);

        String songTitle  = getIntent().getStringExtra("title");
        String songArtist  = getIntent().getStringExtra("artist");
        String path = getIntent().getStringExtra("path");
        String albumId = getIntent().getStringExtra("albumId");
        int imageRes  = getIntent().getIntExtra("image", 0);

        // Set data
        title.setText(songTitle);
        artist.setText(songArtist);

        if (imageRes != 0) {
            img.setImageResource(imageRes);
        }

    }
}